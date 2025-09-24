import React from 'react';

// --- MOCK DATA ---
// Replace this with data fetched from your backend API
const doctorsData = [
    {
        id: 1,
        name: 'Dr. Jane Cooper',
        specialization: 'Cardiology',
        imageUrl: 'https://placehold.co/100x100/e2e8f0/4a5568?text=Dr.+C',
    },
    {
        id: 2,
        name: 'Dr. John Smith',
        specialization: 'Neurology',
        imageUrl: 'https://invalid-url.com/doctor.jpg', // This will intentionally fail to load
    },
    {
        id: 3,
        name: 'Dr. Emily White',
        specialization: 'Pediatrics',
        imageUrl: 'https://placehold.co/100x100/dbeafe/4338ca?text=Dr.+W',
    },
    {
        id: 4,
        name: 'Dr. Michael Brown',
        specialization: 'Orthopedics',
        imageUrl: null, // No image provided
    },
];

// --- SVG Icon for Default Avatar ---
const DoctorIcon = () => (
    <svg
        className="w-16 h-16 text-gray-400"
        fill="currentColor"
        viewBox="0 0 20 20"
        xmlns="http://www.w3.org/2000/svg"
    >
        <path
            fillRule="evenodd"
            d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"
            clipRule="evenodd"
        ></path>
    </svg>
);

// --- Doctor Card Component ---
const DoctorCard = ({ doctor }) => {
    const [imageError, setImageError] = React.useState(false);
    
    const handleImageError = () => {
        setImageError(true);
    };
    
    return (
        <div className="bg-gray-800 rounded-xl p-6 flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-6 transform hover:scale-105 transition-transform duration-300 shadow-lg">
            {/* Profile Picture or Default Icon */}
            <div className="flex-shrink-0 w-24 h-24 rounded-full bg-gray-700 flex items-center justify-center overflow-hidden">
                {doctor.imageUrl && !imageError ? (
                    <img
                        src={doctor.imageUrl}
                        alt={`Dr. ${doctor.name}`}
                        className="w-full h-full object-cover"
                        onError={handleImageError}
                    />
                ) : (
                    <DoctorIcon />
                )}
            </div>
            {/* Doctor Info */}
            <div className="flex-1 text-center sm:text-left">
                <h3 className="text-2xl font-bold text-white">{doctor.name}</h3>
                <p className="text-blue-300 text-md">{doctor.specialization}</p>
            </div>
            {/* Book Appointment Button */}
            <div className="flex-shrink-0">
                <button className="bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                    Book Appointment
                </button>
            </div>
        </div>
    );
};

// --- Main Doctors List Page Component ---
export default function DoctorsListPage() {
  return (
    <div className="bg-gray-900 min-h-screen">
        <div className="px-4 sm:px-8 py-8">
            <h1 className="text-4xl sm:text-5xl font-bold text-white text-center mb-10">
                Find a Doctor
            </h1>
            <div className="space-y-6">
                {doctorsData.map((doctor) => (
                    <DoctorCard key={doctor.id} doctor={doctor} />
                ))}
            </div>
        </div>
    </div>
  );
}
