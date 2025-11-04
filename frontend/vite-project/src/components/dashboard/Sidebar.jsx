import { Link, useLocation } from "react-router-dom";
import { useAuth } from "../../auth/AuthContext.jsx";
import {
  Home,
  Users,
  Stethoscope,
  CalendarDays,
  ClipboardList,
  LogOut,
  MessageSquare,
} from "lucide-react";

export default function Sidebar({ items = [], title = "Dashboard" }) {
  const { pathname } = useLocation();
  const { user, logout } = useAuth();

  return (
    <aside className="w-full md:w-64 bg-gray-800 text-gray-100 min-h-screen md:min-h-[calc(100vh-0px)]">
      <div className="p-4 border-b border-gray-700">
        <div className="text-lg font-semibold">{title}</div>
        <div className="text-sm text-gray-400 mt-1">
          {user?.username} · {user?.role}
        </div>
      </div>
      <nav className="p-2">
        {items.map((item) => (
          <Link
            key={item.to}
            to={item.to}
            className={`flex items-center gap-3 p-3 rounded-md hover:bg-gray-700 transition-colors ${
              pathname === item.to ? "bg-gray-700" : ""
            }`}
          >
            {iconFor(item.icon)}
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>
      <div className="p-2 mt-auto">
        <button
          onClick={logout}
          className="w-full flex items-center gap-3 p-3 rounded-md hover:bg-gray-700 text-left"
        >
          <LogOut className="w-5 h-5" />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
}

function iconFor(name) {
  const className = "w-5 h-5";
  switch (name) {
    case "home":
      return <Home className={className} />;
    case "users":
      return <Users className={className} />;
    case "stethoscope":
      return <Stethoscope className={className} />;
    case "calendar":
      return <CalendarDays className={className} />;
    case "clipboard":
      return <ClipboardList className={className} />;
    case "feedback":
      return <MessageSquare className={className} />;
    default:
      return <Home className={className} />;
  }
}
