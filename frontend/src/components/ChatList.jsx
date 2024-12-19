import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import './ChatList.css';
import Header from './Header';
import {useNavigate } from "react-router-dom";

const MAIN_API_URL = process.env.REACT_APP_MAIN_API_URL;
const RECOMMEND_API_URL = process.env.REACT_APP_RECOMMEND_API_URL;

function ChatList({ username, isLoggedIn, handleLogout }) {
  const [chatRooms, setChatRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [myId, setMyId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchChatRooms = async () => {
      try {
        const response = await axios.post(`${MAIN_API_URL}/chat/list`, {}, {
          withCredentials: true
        });
  
        if (response.data.code === 200) {
          const myId = response.data.myId;
          setMyId(myId);
  
          const filteredRooms = response.data.userMessageList
            .filter(item => 
              item.userList.some(user => user.id !== myId) &&
              item.messageList.length > 0
            )
            .map(item => {
              const latestMessage = item.messageList.reduce((latest, current) => {
                return new Date(latest.createdAt) > new Date(current.createdAt) ? latest : current;
              }, item.messageList[0]);
  
              return {
                ...item.chatRoom,
                latestMessage,
                otherUser: item.userList.find(user => user.id !== myId)
              };
            });
  
          setChatRooms(filteredRooms);
        } else if (response.data.code === 204) {
          setChatRooms([]);
        } else {
          setError('채팅방 목록을 불러오는 데 실패했습니다.');
        }
      } catch (err) {
        setError('채팅방 목록을 불러오는 데 문제가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };
  
    fetchChatRooms();
  }, [username]);
  

  // 로딩 상태 처리
  if (loading) {
    return <p>로딩 중...</p>;
  }

  // 에러 상태 처리
  if (error) {
    return <p>{error}</p>;
  }

  const handleUserClick = (userId) => {
    navigate(`/profile/${userId}`); // userId를 URL 파라미터로 전달
  };
  return (
    <>
        <Header
        isLoggedIn={isLoggedIn}
        username={username}
        onLogout={handleLogout}
        />
      <div className="chat-list-container">
        <div className="chat-list-header">
          <h2>채팅 목록</h2>
          <span className="chat-count">{chatRooms.length}개의 채팅방</span>
        </div>
        <div className="chat-rooms">
          {chatRooms.length === 0 ? ( // 채팅방이 없는 경우
            <div className="no-chat-rooms">
              <p>진행 중인 채팅이 없습니다.</p>
            </div>
          ) : (
            chatRooms.map((room) => (
              <Link
                key={room.id}
                to={`/chat/${room.id}`}
                state={{
                  chatroomId: room.id,
                  myId: myId, // 여기서 myId를 전달
                }}
                className="chat-room-item"
              >
                <div className="user-avatar">
                  <img src={`https://via.placeholder.com/40`} alt="프로필" />
                </div>
                <div className="chat-room-info">
                  <div className="chat-room-header">
                    <h3 onClick={() => handleUserClick(room.otherUser.id)}
                        className="username-link">{room.otherUser.loginId}</h3> {/* 상대방의 loginId */}
                    <span className="last-time">
                      {new Date(room.latestMessage.createdAt).toLocaleString()} {/* 최신 메시지의 시간 */}
                    </span>
                  </div>
                  <div className="chat-room-preview">
                    <p>{room.latestMessage.message}</p> {/* 최신 메시지 */}
                  </div>
                </div>
              </Link>
            ))
          )}
        </div>
      </div>
    </>
  );
}

export default ChatList;
