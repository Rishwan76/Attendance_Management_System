import {useEffect,useState} from 'react';
import {Users,UserCheck,UserX,Percent,CalendarDays} from 'lucide-react';
import {BarChart,Bar,XAxis,YAxis,Tooltip,ResponsiveContainer,PieChart,Pie,Cell} from 'recharts';
import api from '../services/api';
import {PageHeader,StatCard,Loading,ErrorBox} from '../components/UI';

export default function AdminDashboard(){
 const [summary,setSummary]=useState(null),[month,setMonth]=useState([]),[error,setError]=useState('');
 useEffect(()=>{Promise.all([api.get('/reports/summary'),api.get('/reports/monthly',{params:{year:new Date().getFullYear(),month:new Date().getMonth()+1}})]).then(([a,b])=>{setSummary(a.data);setMonth(b.data)}).catch(e=>setError(e.response?.data?.message||'Could not load dashboard'))},[]);
 if(error)return <><PageHeader title="Dashboard"/><ErrorBox error={error}/></>;
 if(!summary)return <Loading/>;
 const pie=[{name:'Present',value:summary.presentToday},{name:'Absent',value:summary.absentToday}];
 return <><PageHeader title="Dashboard" subtitle="Overview of attendance activity"/>
 <div className="stats-grid">
  <StatCard title="Total Students" value={summary.totalStudents} icon={Users}/>
  <StatCard title="Present Today" value={summary.presentToday} icon={UserCheck}/>
  <StatCard title="Absent Today" value={summary.absentToday} icon={UserX}/>
  <StatCard title="Today's Attendance" value={`${summary.todayPercentage}%`} icon={Percent}/>
 </div>
 <div className="charts-grid">
  <div className="panel"><div className="panel-title"><h3>Current Month</h3><span>Attendance by student</span></div>
   <div className="chart"><ResponsiveContainer width="100%" height={300}><BarChart data={month.slice(0,10)}><XAxis dataKey="studentId"/><YAxis/><Tooltip/><Bar dataKey="present" name="Present"/></BarChart></ResponsiveContainer></div>
  </div>
  <div className="panel"><div className="panel-title"><h3>Today</h3><span>Present vs absent</span></div>
   <div className="chart"><ResponsiveContainer width="100%" height={300}><PieChart><Pie data={pie} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={95} label>{pie.map((x,i)=><Cell key={i}/>)}</Pie><Tooltip/></PieChart></ResponsiveContainer></div>
  </div>
 </div>
 <div className="panel"><div className="panel-title"><h3>Monthly Summary</h3><span>Current month</span></div>
 <div className="table-wrap"><table><thead><tr><th>Student</th><th>Department</th><th>Working</th><th>Present</th><th>Absent</th><th>%</th></tr></thead><tbody>{month.map(r=><tr key={r.studentId}><td><b>{r.studentName}</b><small>{r.studentId}</small></td><td>{r.department||'-'}</td><td>{r.workingDays}</td><td>{r.present}</td><td>{r.absent}</td><td><span className={r.percentage<75?'badge danger':'badge success'}>{r.percentage}%</span></td></tr>)}</tbody></table></div></div>
 </>;
}
