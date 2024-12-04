import BasicLayout from "../../../common/layout/BasicLayout";
import HelloComponent from "../component/HelloComponent";
import NavigationMenu from "../component/NavigationMenu";

const MainPage = () => {
    return (
        <BasicLayout>
            <div className="min-w-full">
                메인 영역
                <HelloComponent />
                <NavigationMenu />
            </div>
        </BasicLayout>
    );
};

export default MainPage;
