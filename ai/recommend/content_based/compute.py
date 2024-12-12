from config import DEFAULT_MODEL_NAME  # 설정 파일에서 모델명 가져오기
from precompute import PrecomputeEmbeddings, sanitize_filename
from database import get_db
import os


def get_model_dir(model_name: str) -> str:
    """
    모델명을 기반으로 안전한 경로 생성
    """
    return os.path.join("embeddings", sanitize_filename(model_name))


if __name__ == "__main__":
    db = next(get_db())

    # 설정에서 모델명 로드
    model_name = DEFAULT_MODEL_NAME
    embeddings_dir = get_model_dir(model_name)
    os.makedirs(embeddings_dir, exist_ok=True)

    # PrecomputeEmbeddings 사용
    precompute = PrecomputeEmbeddings(model_name=model_name)
    precompute.save_embeddings_and_similarities(
        db=db,
        embeddings_file=os.path.join(embeddings_dir, "embeddings.npy"),
        similarities_file=os.path.join(embeddings_dir, "similarities.npy"),
    )

    print(f"임베딩 및 유사도 파일이 '{embeddings_dir}'에 저장되었습니다.")