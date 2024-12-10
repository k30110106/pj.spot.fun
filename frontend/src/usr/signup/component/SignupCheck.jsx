import React, { useState } from "react";
import styled from "styled-components";
import { MdOutlineKeyboardArrowDown } from "react-icons/md";
import { MdOutlineKeyboardArrowUp } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import { Button } from "@mui/material";

const SignupCheck = () => {
  const navigate = useNavigate();
  const [open1, setOpen1] = useState(false);
  const [open2, setOpen2] = useState(false);
  const [open3, setOpen3] = useState(false);
  const [open4, setOpen4] = useState(false);

  const data = [
    {
      id: 0,
      title: "만 14세 이상입니다.",
      contents:
        "멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다.",
      status: "[필수]",
    },
    {
      id: 1,
      title: "FunSpot 서비스 이용약관 동의",
      contents:
        "멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다. 멤버십 필수 약관에 동의합니다.",
      status: "[필수]",
    },
    {
      id: 2,
      title: "개인정보 수집 및 이용 동의",
      contents: "반갑습니다",
      status: "[필수]",
    },
    {
      id: 3,
      title: "마케팅 정보 수신에 대한 동의",
      contents: "선택입니다",
      status: "[선택]",
    },
  ];

  const [checkItems, setCheckItems] = useState([]);

  // 필수 항목 확인
  const requiredIds = data
    .filter((item) => item.status === "[필수]")
    .map((item) => item.id);

  // 체크박스 개별 선택하기
  const selectChecked = (checked, id) => {
    if (checked) {
      setCheckItems((item) => [...item, id]);
    } else {
      setCheckItems(checkItems.filter((el) => el !== id));
    }
  };

  // 체크박스 전체 선택하기
  const allChecked = (checked) => {
    if (checked) {
      const itemList = [];
      data.forEach((el) => itemList.push(el.id));
      setCheckItems(itemList);
    } else {
      setCheckItems([]);
    }
  };

  // 동의하고 진행하기 버튼 클릭 시 처리
  const handleSubmit = () => {
    const hasAllRequiredChecked = requiredIds.every((id) =>
      checkItems.includes(id)
    );
    if (!hasAllRequiredChecked) {
      alert("필수사항을 모두 체크해주세요.");
      return;
    }
    // 회원가입 페이지로 이동
    navigate("/signup"); // "/signup" 경로는 회원가입 페이지
  };

  return (
    <Wrap>
      {/* 모두 동의합니다 부분 */}
      <AllAgreeWrap>
        <label>
          <input
            type="checkbox"
            name="all-checked"
            onChange={(e) => allChecked(e.target.checked)}
            checked={checkItems.length === data.length}
          />
          모두 동의합니다.
        </label>
        <IconWrap>
          {open1 && open2 && open3 && open4 ? (
            <MdOutlineKeyboardArrowUp
              size={30}
              color="gray"
              onClick={() => {
                setOpen1(!open1);
                setOpen2(!open2);
                setOpen3(!open3);
                setOpen4(!open4);
              }}
            />
          ) : (
            <MdOutlineKeyboardArrowDown
              size={30}
              color="gray"
              onClick={() => {
                setOpen1(!open1);
                setOpen2(!open2);
                setOpen3(!open3);
                setOpen4(!open4);
              }}
            />
          )}
        </IconWrap>
      </AllAgreeWrap>
      <hr />

      {/* 개별 선택 부분 */}
      {data.map((item, index) => (
        <div key={item.id}>
          <ItemWrap>
            <Title>
              <input
                type="checkbox"
                name="select-checked"
                onChange={(e) => selectChecked(e.target.checked, item.id)}
                checked={checkItems.includes(item.id)}
              />
              <span
                style={{
                  marginRight: "5px",
                  color: item.status === "[필수]" ? "red" : "gray",
                }}
              >
                {item.status}
              </span>{" "}
              {item.title}
            </Title>
            <IconWrap>
              {(index === 0 && open1) ||
              (index === 1 && open2) ||
              (index === 2 && open3) ||
              (index === 3 && open4) ? (
                <MdOutlineKeyboardArrowUp
                  size={30}
                  color="gray"
                  onClick={() => {
                    if (index === 0) setOpen1(!open1);
                    else if (index === 1) setOpen2(!open2);
                    else if (index === 2) setOpen3(!open3);
                    else if (index === 3) setOpen4(!open4);
                  }}
                />
              ) : (
                <MdOutlineKeyboardArrowDown
                  size={30}
                  color="gray"
                  onClick={() => {
                    if (index === 0) setOpen1(!open1);
                    else if (index === 1) setOpen2(!open2);
                    else if (index === 2) setOpen3(!open3);
                    else if (index === 3) setOpen4(!open4);
                  }}
                />
              )}
            </IconWrap>
          </ItemWrap>
          {(index === 0 && open1) ||
          (index === 1 && open2) ||
          (index === 2 && open3) ||
          (index === 3 && open4) ? (
            <ContentsWrap>
              <p>{item.contents}</p>
            </ContentsWrap>
          ) : null}
        </div>
      ))}
      <Button onClick={handleSubmit}>동의하고 진행하기</Button>
    </Wrap>
  );
};

// 스타일 컴포넌트
const Wrap = styled.div`
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 400px;
  margin: 20px auto;
`;

const ContentsWrap = styled.div`
  background-color: #f9f9f9;
  margin-top: 10px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
`;

const ItemWrap = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between; /* 제목과 아이콘을 양쪽 끝으로 배치 */
  margin: 10px 0;
`;

const Title = styled.label`
  display: flex;
  align-items: center; /* 체크박스와 텍스트를 세로로 정렬 */
`;

const IconWrap = styled.div`
  cursor: pointer;
`;

const AllAgreeWrap = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between; /* "모두 동의합니다"와 아이콘을 양쪽 끝으로 배치 */
  margin-bottom: 15px;
`;
export default SignupCheck;
