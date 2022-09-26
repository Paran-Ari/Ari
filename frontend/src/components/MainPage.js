import React, { useEffect, useState } from "react";
import "./MainPage.css";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

const MainPage = ({ onClick }) => {
  // 가게 카테고리
  const menuRow1 = [
    { name: "한식", image: "images/koreanFood.png" },
    { name: "양식", image: "images/westernFood.png" },
    { name: "일식", image: "images/japaneseFood.png" },
    { name: "패스트푸드", image: "images/fastFood.png" },
  ];

  const menuRow2 = [
    { name: "카페", image: "images/cafe.png" },
    { name: "헤어", image: "images/hairshop.png" },
    { name: "PC방", image: "images/pcBang.png" },
    { name: "노래방", image: "images/karaoke.png" },
  ];

  // 이벤트 배너
  const banner = [
    { name: "모서리다방", image: "images/defaultEventBanner.png" },
    { name: "미스터쉐프", image: "images/defaultEventBanner.png" },
    { name: "맥도날드", image: "images/defaultEventBanner.png" },
    { name: "아맛집", image: "images/defaultEventBanner.png" },
    { name: "쉐프의 포차", image: "images/defaultEventBanner.png" },
  ];

  // react-slick 캐러셀 설정
  const settings = {
    infinite: true, // 무한 캐러셀
    speed: 500, // 다음 컨텐츠 까지의 속도
    slidesToShow: 1, // 화면에 보이는 컨텐츠 수
    slidesToScroll: 1, // 스크롤 시 넘어가는 컨텐츠 수
    autoplay: true, // 자동 캐러셀
    autoplaySpeed: 3000, // 자동 캐러셀 속도
    draggable: true, // 드래그
    pauseOnFocus: true, // focus시 정지
    pauseOnHover: true, // hover시 정지
  };

  return (
    <>
      <div
        onClick={onClick}
        style={{ display: "flex", justifyContent: "center" }}
      >
        <div style={{ width: "375px" }}>
          <div style={{ padding: "0 28px", marginTop: "85px" }}>
            <div className="mainPage-flex-column-end">
              <p className="mainPage-intro">우리 주변 제휴 정보</p>
              <p className="mainPage-intro">
                <span style={{ color: "#386FFE" }}>아리</span>에서
              </p>
            </div>
            <input
              className="mainPage-store-search"
              placeholder="제휴 정보를 보고 싶은 가게명을 검색하세요!"
            ></input>
          </div>
          <Slider {...settings}>
            {banner.map((event, index) => {
              return (
                <div key={index} className="mainPage-banner-box">
                  <img alt="" src="images/defaultEventBanner.png"></img>
                  <p className="mainPage-banner-intro">
                    {event.name}
                    <br />
                    제휴 이벤트 확인하기
                  </p>
                  <div className="mainPage-banner-num">
                    {index}/{banner.length}
                  </div>
                </div>
              );
            })}
          </Slider>
          <div style={{ padding: "0 28px", marginTop: "40px" }}>
            <p className="mainPage-subIntro">
              <span className="mainPage-select-intro">골라서</span>
              보는 건 어때요?
            </p>
            <table>
              <tbody>
                <tr>
                  {menuRow1.map((menu, index) => {
                    return (
                      <td key={index}>
                        <div
                          className="mainPage-flex-column-center"
                          style={{ margin: "0 11px 34px 11px" }}
                        >
                          <img alt="" src={menu.image}></img>
                          <span style={{ marginTop: "9px", fontSize: "12px" }}>
                            {menu.name}
                          </span>
                        </div>
                      </td>
                    );
                  })}
                </tr>
                <tr>
                  {menuRow2.map((menu, index) => {
                    return (
                      <td key={index}>
                        <div
                          className="mainPage-flex-column-center"
                          style={{ margin: "0 11px 34px 11px" }}
                        >
                          <img alt="" src={menu.image}></img>
                          <span style={{ marginTop: "9px", fontSize: "12px" }}>
                            {menu.name}
                          </span>
                        </div>
                      </td>
                    );
                  })}
                </tr>
              </tbody>
            </table>
            <div style={{ clear: "both" }}></div>
            <p className="mainPage-subIntro">전체 목록 보기!</p>
            <div style={{ float: "left", marginLeft: "10px" }}>
              <div className="mainPage-flex-column-center">
                <img alt="" src="images/seeAll.png"></img>
                <span style={{ marginTop: "9px", fontSize: "12px" }}>전체</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="footer">
        <p>아리&#40;로고&#41; 팀 정보</p>
        <table>
          <tbody>
            <tr>
              <td>팀장</td>
              <td>여인수</td>
            </tr>
            <tr>
              <td>대표 이메일</td>
              <td>aritest0222@gmail.com</td>
            </tr>
            <tr>
              <td>대표 연락처</td>
              <td>010-4355-4616</td>
            </tr>
          </tbody>
        </table>
      </div>
    </>
  );
};

export default MainPage;