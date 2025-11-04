export default function Footer() {
  return (
    <footer className="bg-black text-gray-400 py-12 px-8 md:px-16 border-t border-gray-800">
      <div className="container mx-auto">
        <div className="text-center mb-8">
          <a href="#features" className="px-3 hover:text-white">
            Features
          </a>
          <span className="text-gray-600">|</span>
          <a href="#features" className="px-3 hover:text-white">
           Feedback 
          </a>
        </div>
        <div className="text-center text-sm">
          <p>1717 Harrison St, San Francisco, CA 94103, USA</p>
          <p className="mt-2">
            &copy; {new Date().getFullYear()} Your Company. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}
