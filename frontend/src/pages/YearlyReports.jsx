import {useEffect,useState} from 'react';
import {Download} from 'lucide-react';
import api from '../services/api';
import {PageHeader,Loading,Empty} from '../components/UI';

function csv(rows){const head=['Student ID','Name','Department','Working Days','Present','Absent','Percentage'];return [head,...rows.map(r=>[r.studentId,r.studentName,r.department||'',r.workingDays,r.present,r.absent,r.percentage+'%'])].map(r=>r.map(v=>`"${String(v).replaceAll('"','""')}"`).join(',')).join('\n')}
export default function YearlyReports(){
 const [year,setYear]=useState(new Date().getFullYear()),[studentId,setStudentId]=useState(''),[department,setDepartment]=useState(''),[rows,setRows]=useState([]),[loading,setLoading]=useState(false);
 async function load(){setLoading(true);setRows((await api.get('/reports/yearly',{params:{year,studentId,department}})).data);setLoading(false)}
 useEffect(()=>{load()},[]);
 function exportCsv(){const blob=new Blob([csv(rows)],{type:'text/csv'}),url=URL.createObjectURL(blob),a=document.createElement('a');a.href=url;a.download=`attendance-year-${year}.csv`;a.click();URL.revokeObjectURL(url)}
 return <><PageHeader title="Yearly Reports" subtitle="Annual attendance summary"><button className="secondary" onClick={exportCsv}><Download size={18}/> Export CSV</button></PageHeader>
 <div className="panel filters"><input type="number" value={year} onChange={e=>setYear(Number(e.target.value))}/><input placeholder="Student ID" value={studentId} onChange={e=>setStudentId(e.target.value)}/><input placeholder="Department" value={department} onChange={e=>setDepartment(e.target.value)}/><button className="primary" onClick={load}>Apply</button></div>
 <div className="panel"><div className="table-wrap">{loading?<Loading/>:<table><thead><tr><th>Student</th><th>Department</th><th>Working</th><th>Present</th><th>Absent</th><th>Attendance</th></tr></thead><tbody>{rows.map(r=><tr key={r.studentId}><td><b>{r.studentName}</b><small>{r.studentId}</small></td><td>{r.department||'-'}</td><td>{r.workingDays}</td><td>{r.present}</td><td>{r.absent}</td><td><span className={r.percentage<75?'badge danger':'badge success'}>{r.percentage}%</span></td></tr>)}</tbody></table>}{!loading&&rows.length===0&&<Empty/>}</div></div>
 </>;
}