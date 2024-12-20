from sqlalchemy import Column, Integer, String, Text, Date, Float, TIMESTAMP
from database import Base


class Book(Base):
    __tablename__ = "book"

    isbn13 = Column(String(255), primary_key=True, index=True)
    title = Column(String(255), nullable=False)
    author = Column(String(255), nullable=False)
    category = Column(String(255))
    description = Column(Text)
    cover = Column(String(512))
    publisher = Column(String(255))
    pubdate = Column(Date)


class BookRegister(Base):
    __tablename__ = "book_register"

    id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    userId = Column(Integer, nullable=False)
    title = Column(String(512), nullable=False)
    isbn13 = Column(String(255), nullable=False)
    review = Column(Text, nullable=False)
    point = Column(String(1), nullable=False)
    b_condition = Column(String(1))
    description = Column(Text)
    exchange_YN = Column(String(1), nullable=False)
    place = Column(Text)
    createdAt = Column(Date)
    updatedAt = Column(Date)


class Reviews(Base):
    __tablename__ = "reviews"
    id = Column(Integer, primary_key=True, autoincrement=True)
    isbn13 = Column(String(255), index=True)
    review_content = Column(Text, nullable=False)


class Keywords(Base):
    __tablename__ = "keywords"
    id = Column(Integer, primary_key=True, autoincrement=True)
    isbn13 = Column(String(255), nullable=False, index=True)
    keyword = Column(String(255), nullable=False)
    frequency = Column(Float, nullable=False)
    created_at = Column(TIMESTAMP, nullable=False)


class KeywordReviews(Base):
    __tablename__ = "keyword_reviews"
    id = Column(Integer, primary_key=True, autoincrement=True)
    isbn13 = Column(String(255), nullable=False, index=True)
    keyword = Column(String(255), nullable=False)
    review_id = Column(Integer, nullable=False)
    created_at = Column(TIMESTAMP, nullable=False)
