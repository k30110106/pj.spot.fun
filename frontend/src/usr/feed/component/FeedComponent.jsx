import React from "react";
import ListItemComponent from "./ListItemComponent";
import { getFeedListApi } from "../api/FeedApi";

const FeedComponent = () => {
  return (
    <div>
      피드 컴포넌트
      <ListItemComponent />
    </div>
  );
};

export default FeedComponent;
