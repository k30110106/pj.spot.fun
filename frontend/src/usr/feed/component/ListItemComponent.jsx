import React, { useEffect, useState } from "react";
import { API_BASE_URL, getFeedListApi } from "../api/FeedApi";

const initState = {
  feedDTOS: [],
  hasNext: false,
};

const ListItemComponent = () => {
  const [serverData, setServerData] = useState(initState);

  useEffect(() => {
    getFeedListApi()
      .then((data) => {
        console.log(data);
        setServerData(data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  console.log(serverData.hasNext);

  return (
    <div>
      {serverData.hasNext ? "OK" : "NO"}
      {serverData.feedDTOS.map((item, index) => {
        return (
          <div key={index}>
            idx : {item.idx}
            <br />
            content : {item.content}
            <br />
            <div>
              <img
                alt="product"
                className="m-auto rounded-md w-60"
                // src={`${API_BASE_URL}/api/products/view/s_${product.uploadFileNames[0]}`}
              />
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default ListItemComponent;
