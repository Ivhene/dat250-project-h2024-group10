import React, { useState, useEffect, useRef } from "react";
import LandingPage from "./components/landingPage/LandingPage";
import FeedPage from "./components/feedPage/FeedPage";
import UserPage from "./components/userPage/UserPage";
import CommonNavbar from "./components/commons/CommonNavbar";

function App() {
  const [activePage, setActivePage] = useState("landing");
  const previousPage = useRef<string | null>(null);
  const isInitialRender = useRef(true);

  const handleNavigation = (page: string) => {
    setActivePage(page);
  };

  useEffect(() => {
    const measureRenderTime = (from: string | null, to: string) => {
      const startMark = from
        ? `navigation-start-${from}`
        : "initial-render-start";
      const endMark = `navigation-end-${to}`;
      const measureName = from
        ? `Render Time from '${from}' to '${to}'`
        : `Initial Render Time for '${to}'`;

      // Start measuring render time
      performance.mark(startMark);

      // Introduce a slight delay to ensure rendering is fully complete
      setTimeout(() => {
        performance.mark(endMark);
        performance.measure(measureName, startMark, endMark);

        const measures = performance.getEntriesByName(measureName);
        if (measures.length > 0) {
          console.log(`${measureName}:`, measures[0].duration, "ms");
        }

        // Clear performance entries
        performance.clearMarks(startMark);
        performance.clearMarks(endMark);
        performance.clearMeasures(measureName);
      }, 1000); // Adjust delay based on your app's complexity
    };

    if (isInitialRender.current) {
      measureRenderTime(null, activePage);
      isInitialRender.current = false;
    } else {
      measureRenderTime(previousPage.current, activePage);
    }

    // Update previous page
    previousPage.current = activePage;
  }, [activePage]);

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
