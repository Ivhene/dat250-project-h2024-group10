"use client";

import React, { useEffect, useRef } from "react";
import { usePathname } from "next/navigation";

export default function AppWrapper({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  const previousPath = useRef<string | null>(null);
  const isInitialRender = useRef(true); // Tracks if it's the first render

  useEffect(() => {
    const measureRenderTime = (from: string | null, to: string) => {
      const startMark = from
        ? `navigation-start-${from}`
        : "initial-render-start";
      const endMark = `navigation-end-${to}`;
      const measureName = from
        ? `Render Time from '${from}' to '${to}'`
        : `Initial Render Time for '${to}'`;

      // Mark the start of rendering
      performance.mark(startMark);

      // Wait until the next animation frame + a small delay to ensure React has completed rendering
      requestAnimationFrame(() => {
        setTimeout(() => {
          performance.mark(endMark);
          performance.measure(measureName, startMark, endMark);

          const measures = performance.getEntriesByName(measureName);
          if (measures.length > 0) {
            console.log(`${measureName}:`, measures[0].duration, "ms");
          }

          // Clear marks and measures to avoid conflicts
          performance.clearMarks(startMark);
          performance.clearMarks(endMark);
          performance.clearMeasures(measureName);
        }, 1000); // Adjust delay as necessary (50ms works for most cases)
      });
    };

    if (isInitialRender.current) {
      // Handle first-time render
      measureRenderTime(null, pathname);
      isInitialRender.current = false;
    } else {
      // Handle navigation render
      measureRenderTime(previousPath.current, pathname);
    }

    // Update the previous path to the current pathname
    previousPath.current = pathname;
  }, [pathname]);

  return <>{children}</>;
}
