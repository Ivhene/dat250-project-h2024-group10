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

      performance.mark(startMark);
      requestAnimationFrame(() => {
        performance.mark(endMark);
        performance.measure("Render Time", startMark, endMark);

        const measures = performance.getEntriesByName("Render Time");
        if (measures.length > 0) {
          if (from) {
            console.log(
              `Render Time from '${from}' to '${to}':`,
              measures[0].duration,
              "ms"
            );
          } else {
            console.log(
              `Initial Render Time for '${to}':`,
              measures[0].duration,
              "ms"
            );
          }
        }
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
