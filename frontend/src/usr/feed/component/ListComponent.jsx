import ProfileComponent from "./item/ProfileComponent";
import ImageComponent from "./item/ImageComponent";
import ButtonComponent from "./item/ButtonComponent";
import ContentComponent from "./item/ContentComponent";

const ListComponent = ({
  feedList,
  openDetailModal,
  handleLikesEvent,
  handleListDeleteEvent,
  openModifyModal,
}) => {
  return (
    <div className="border-2 border-green-200 w-full">
      <h1>Feed List Component</h1>
      {(feedList ?? []).length > 0 ? (
        feedList.map((feed) => (
          <div
            key={feed.idx}
            className="border border-blue-500 rounded-lg h-1/12 py-4"
          >
            <ProfileComponent
              feedUserInfo={feed.user}
              pageType={"list"}
              handleListDeleteEvent={() => handleListDeleteEvent(feed.idx)}
              openModifyModal={() => openModifyModal(feed.idx)}
            />

            <div className="flex">
              <div className="w-full relative">
                {feed.feedImages.length > 0 && (
                  <ImageComponent
                    openDetailModal={() => openDetailModal(feed.idx)}
                    feedImages={feed.feedImages}
                  />
                )}
              </div>
            </div>

            <ButtonComponent
              feed={feed}
              openDetailModal={() => openDetailModal(feed.idx)}
              handleLikesEvent={handleLikesEvent}
            />
            <ContentComponent
              feed={feed}
              openDetailModal={() => openDetailModal(feed.idx)}
            />
          </div>
        ))
      ) : (
        <div>NO Data</div>
      )}
    </div>
  );
};

export default ListComponent;
