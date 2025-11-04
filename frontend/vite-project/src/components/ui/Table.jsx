import React from "react";

export default function Table({ columns, data, renderActions }) {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-sm text-gray-200">
        <thead className="bg-gray-800 text-gray-300">
          <tr>
            {columns.map((col) => (
              <th
                key={col.key}
                className="px-4 py-2 text-left font-medium border-b border-gray-700"
              >
                {col.header}
              </th>
            ))}
            {renderActions && (
              <th className="px-4 py-2 text-left font-medium border-b border-gray-700">
                Actions
              </th>
            )}
          </tr>
        </thead>
        <tbody>
          {data.map((row, idx) => (
            <tr key={idx} className="odd:bg-gray-900 even:bg-gray-800">
              {columns.map((col) => (
                <td
                  key={col.key}
                  className="px-4 py-2 border-b border-gray-700"
                >
                  {col.render ? col.render(row[col.key], row) : row[col.key]}
                </td>
              ))}
              {renderActions && (
                <td className="px-4 py-2 border-b border-gray-700">
                  <div className="flex gap-2">{renderActions(row)}</div>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
