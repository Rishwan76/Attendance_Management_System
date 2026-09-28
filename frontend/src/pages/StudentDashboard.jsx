import {useEffect,useState} from 'react';
import {CalendarDays,UserCheck,UserX,Percent} from 'lucide-react';
import {PieChart,Pie,Tooltip,ResponsiveContainer,Cell} from 'recharts';
import api from '../services/api';
import {useAuth} from '../context/AuthContext';
import {PageHeader,StatCard,Loading} from '../components/UI';

export default function StudentDashboard(){
 const {user}=useAuth();const [profile,setProfile]=useState(null),[rows,setRows]=useState([]);
 useEffect(()=>{Promise.all([api.get('/me/student'),api.get('/me/attendance')]).then(([a,b])=>{setProfile(a.data);setRows(b.data)})},[]);
 if(!profile)return <Loading/>;
 const present=rows.filter(x=>x.status==='PRESENT').length, absent=rows.filter(x=>x.status==='ABSENT').length, total=present+absent, pct=total?Math.round(present*10000/total)/100:0;
 const pie=[{name:'Present',value:present},{name:'Absent',value:absent}];
 return <><PageHeader title={`Welcome, ${profile.name}`} subtitle={`${profile.studentId} • ${profile.department||'Student'}`}/>
 <div className="stats-grid"><StatCard title="Working Days" value={total} icon={CalendarDays}/><StatCard title="Present Days" value={present} icon={UserCheck}/><StatCard title="Absent Days" value={absent} icon={UserX}/><StatCard title="Attendance" value={`${pct}%`} icon={Percent}/></div>
 <div className="charts-grid">
  <div className="panel"><div className="panel-title"><h3>Attendance Percentage</h3></div><div className="chart"><ResponsiveContainer width="100%" height={280}><PieChart><Pie data={pie} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={95} label>{pie.map((x,i)=><Cell key={i}/>)}</Pie><Tooltip/></PieChart></ResponsiveContainer></div></div>
  <div className="panel"><div className="panel-title"><h3>Recent Attendance</h3></div><div className="table-wrap"><table><thead><tr><th>Date</th><th>Status</th><th>Marked By</th></tr></thead><tbody>{rows.slice(0,8).map(r=><tr key={r.id}><td>{r.attendanceDate}</td><td><span className={r.status==='PRESENT'?'badge success':'badge danger'}>{r.status}</span></td><td>{r.markedBy}</td></tr>)}</tbody></table></div></div>
 </div></>
}
