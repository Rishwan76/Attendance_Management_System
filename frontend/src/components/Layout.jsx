import {NavLink, Outlet} from 'react-router-dom';
import {LayoutDashboard, Users, ClipboardCheck, FileBarChart, UserCircle, LogOut, Menu, X} from 'lucide-react';
import {useAuth} from '../context/AuthContext';
import {useState} from 'react';
import '../styles.css';

export default function Layout() {
  const {user, logout} = useAuth();
  const [open,setOpen]=useState(false);
  const admin=user?.role==='ADMIN'||user?.role==='STAFF';
  const links=admin ? [
    ['/app','Dashboard',LayoutDashboard],
    ['/app/students','Students',Users],
    ['/app/attendance','Daily Attendance',ClipboardCheck],
    ['/app/history','Attendance History',ClipboardCheck],
    ['/app/reports','Monthly Reports',FileBarChart],
    ['/app/yearly-reports','Yearly Reports',FileBarChart],
    ['/app/profile','Profile',UserCircle]
  ] : [
    ['/app','Dashboard',LayoutDashboard],
    ['/app/my-attendance','My Attendance',ClipboardCheck],
    ['/app/profile','Profile',UserCircle]
  ];
  return <div className="shell">
    <button className="mobile-menu" onClick={()=>setOpen(!open)}>{open?<X/>:<Menu/>}</button>
    <aside className={open?'sidebar open':'sidebar'}>
      <div className="brand"><div className="logo">A</div><div><b>AttendPro</b><small>Management System</small></div></div>
      <nav>{links.map(([to,label,Icon])=><NavLink key={to} to={to} end={to==='/app'} onClick={()=>setOpen(false)}><Icon size={18}/>{label}</NavLink>)}</nav>
      <div className="sidebar-bottom">
        <div className="user-mini"><div className="avatar">{user?.username?.[0]?.toUpperCase()}</div><div><b>{user?.username}</b><small>{user?.role}</small></div></div>
        <button className="logout" onClick={logout}><LogOut size={18}/> Logout</button>
      </div>
    </aside>
    <main className="main"><Outlet/></main>
  </div>
}
