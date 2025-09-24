import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import DoctorsListPage from './DoctorsListPage.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <DoctorsListPage />
  </StrictMode>,
)
