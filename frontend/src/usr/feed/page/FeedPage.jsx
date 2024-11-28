import BasicLayout from "../../../common/layout/BasicLayout";
import FeedComponent from "../component/FeedComponent";

const FeedPage = () => {
    return (
        <BasicLayout>
            <div id="login-page">
                피드 페이지
                <FeedComponent/>
            </div>
        </BasicLayout>
    );
};

export default FeedPage;
