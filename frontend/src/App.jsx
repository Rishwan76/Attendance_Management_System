import {Routes,Route,Navigate} from 'react-router-dom';
import {useAuth} from './context/AuthContext';
import Layout from './components/Layout';
import ProtectedRoute from './routes/ProtectedRoute';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import StudentDashboard from './pages/StudentDashboard';
import Students from './pages/Students';
import StudentForm from './pages/StudentForm';
import DailyAttendance from './pages/DailyAttendance';
import History from './pages/History';
import Reports from './pages/Reports';
import YearlyReports from './pages/YearlyReports';
import MyAttendance from './pages/MyAttendance';
import Profile from './pages/Profile';

export default function App(){
 const {user}=useAuth();
 return <Routes>
   <Route path="/" element={user?<Navigate to="/app"/>:<Login/>}/>
   <Route element={<ProtectedRoute/>}><Route path="/app" element={<Layout/>}>
     <Route index element={user?.role==='STUDENT'?<StudentDashboard/>:<AdminDashboard/>}/>
     <Route path="profile" element={<Profile/>}/>
     <Route element={<ProtectedRoute roles={['ADMIN','STAFF']}/>}>
       <Route path="students" element={<Students/>}/>
       <Route path="students/new" element={<StudentForm/>}/>
       <Route path="students/:id/edit" element={<StudentForm/>}/>
       <Route path="attendance" element={<DailyAttendance/>}/>
       <Route path="history" element={<History/>}/>
       <Route path="reports" element={<Reports/>}/>
       <Route path="yearly-reports" element={<YearlyReports/>}/>
     </Route>
     <Route element={<ProtectedRoute roles={['STUDENT']}/>}>
       <Route path="my-attendance" element={<MyAttendance/>}/>
     </Route>
   </Route></Route>
   <Route path="*" element={<Navigate to="/app"/>}/>
 </Routes>
}
