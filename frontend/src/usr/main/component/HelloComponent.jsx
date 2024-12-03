import React, { useEffect } from "react";
import { getHelloApi } from "../api/MainApi";

const HelloComponent = () => {
  const [hello, setHello] = React.useState("");
  const [accessToken, setAccessToken] = React.useState("");

  useEffect(() => {
    getHelloApi((result) => {
      if (result.status !== 200) {
        setHello("api get failed");
        return;
      }
      console.log(result);
      setHello(result.data.message);
    });
  }, []);

  return (
    <div>
      API Hello API TEXT = {hello}
      <br />
      AccessToken Value = {accessToken}
    </div>
  );
};

export default HelloComponent;
