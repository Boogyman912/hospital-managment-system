import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="bg-gray-900 text-white py-4 px-8 md:px-16 border-b border-gray-800">
      <div className="container mx-auto flex justify-between items-center">
        <Link to="/" className="text-2xl font-bold">
          Hospital Management
        </Link>
        <nav className="hidden md:flex items-center space-x-8">
          <Link to="/" className="hover:text-blue-400 transition-colors">
            Home
          </Link>
          <a href="#about" className="hover:text-blue-400 transition-colors">
            About Us
          </a>
          <Link to="/doctors" className="hover:text-blue-400 transition-colors">
            Doctors
          </Link>
          <Link
            to="/appointments"
            className="hover:text-blue-400 transition-colors"
          >
            My Appointments
          </Link>
          <Link
            to="/login"
            className="bg-gray-800 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded-lg transition-colors"
          >
            Login
          </Link>
        </nav>
      </div>
    </header>
  );
}
