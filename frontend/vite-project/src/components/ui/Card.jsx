import React from "react";

export default function Card({ title, actions, children }) {
  return (
    <div className="bg-gray-800 border border-gray-700 rounded-lg p-4">
      {(title || actions) && (
        <div className="flex items-center justify-between mb-3">
          {title && <h3 className="text-gray-100 font-semibold">{title}</h3>}
          {actions && <div className="flex gap-2">{actions}</div>}
        </div>
      )}
      <div className="text-gray-200">{children}</div>
    </div>
  );
}
