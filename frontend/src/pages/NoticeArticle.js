import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import Header from "../components/Header";
import Loading from "../components/Loading";
import { authState } from "../state";
import { customAxios } from "./customAxios";
import "./NoticeArticle.css";
import "./NoticeWrite.css";

const NoticeArticle = () => {
  const [data, setData] = useState();
  const { articleId } = useParams();
  const [auth, setAuth] = useRecoilState(authState);
  const navigate = useNavigate();
  const testAuth = 3;
  useEffect(() => {
    customAxios.get(`/admin/notice/${articleId}`).then((res) => {
      setData(res.data);
    });
  }, []);
  const onClick = () => {
    console.log(articleId);
    navigate(`/notice/modify/${articleId}`);
  };
  const onClickDelete = () => {
    console.log(articleId, "삭제");
  };
  if (!data) {
    return <Loading></Loading>;
  } else {
    return (
      <>
        <Header text="공지사항" back={true}></Header>
        <div className="noticeArticleContainer">
          <div className="noticeArticleHeader">
            <span className="noticeArticleTitle">{data.title}</span>
            <span className="noticeArticleDate">{data.createDate}</span>
          </div>
          <div className="noticeArticleContent">
            <span className="noticleArticleText">{data.content}</span>
          </div>
        </div>
        {testAuth === 3 && (
          <div className="noticeWriteBtnBox">
            <button className="noticeModifyBtn" onClick={onClick}>
              수정
            </button>
            <button className="noticedeleteBtn" onClick={onClickDelete}>
              삭제
            </button>
          </div>
        )}
      </>
    );
  }
};

export default NoticeArticle;