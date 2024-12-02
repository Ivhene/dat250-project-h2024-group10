import React, { useState } from "react";
import LandingPage from "./components/landingPage/LandingPage";
import FeedPage from "./components/feedPage/FeedPage";
import UserPage from "./components/userPage/UserPage";
import CommonNavbar from "./components/commons/CommonNavbar";

function App() {
  const [activePage, setActivePage] = useState("landing");

  const handleNavigation = (page: string) => {
    setActivePage(page);
  };

  return (
    <div className="w-full h-screen flex flex-col">
      <CommonNavbar navigate={handleNavigation} />
      <div className="flex-grow">
        {activePage === "landing" && <LandingPage />}
        {activePage === "feed" && <FeedPage />}
        {activePage === "user" && <UserPage />}
      </div>
    </div>
  );
}

export default App;
