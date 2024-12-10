const MenuBarComponent = ({ menu, activeMenu, handleMenuTabClickEvent }) => {
  const { id, name } = menu;

  return (
    <div
      className={`tab text-lg font-semibold text-gray-800 hover:text-blue-500 cursor-pointer 
        px-4 py-2 rounded-lg transition duration-300 ease-in-out transform 
        ${
          activeMenu === id
            ? "bg-blue-100 scale-105"
            : "hover:bg-blue-100 hover:scale-105"
        }`}
      onClick={() => handleMenuTabClickEvent(id)}
    >
      {name}
    </div>
  );
};

export default MenuBarComponent;
