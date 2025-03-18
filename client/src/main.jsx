import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-icons/font/bootstrap-icons.css'
import "./css/main.css";

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <> 
    <App />
    </>
  </StrictMode>,
)
