import {useEffect,useState} from 'react';
import api from '../services/api';
import {PageHeader,Loading,Empty} from '../components/UI';

export default function History(){
 const [date,setDate]=useState(new Date().toISOString().slice(0,10)),[rows,setRows]=useState([]),[loading,setLoading]=useState(true);
 async function load(){setLoading(true);setRows((await api.get('/attendance/date/'+date)).data);setLoading(false)}
 useEffect(()=>{load()},[date]);
 return <><PageHeader title="Attendance History" subtitle="View attendance records by date"><input className="date-control" type="date" value={date} onChange={e=>setDate(e.target.value)}/></PageHeader>
 <div className="panel"><div className="table-wrap">{loading?<Loading/>:<table><thead><tr><th>Date</th><th>Student</th><th>Status</th><th>Marked By</th><th>Updated</th></tr></thead><tbody>{rows.map(r=><tr key={r.id}><td>{r.attendanceDate}</td><td><b>{r.studentName}</b><small>{r.studentCode}</small></td><td><span className={r.status==='PRESENT'?'badge success':'badge danger'}>{r.status}</span></td><td>{r.markedBy}</td><td>{new Date(r.updatedAt).toLocaleString()}</td></tr>)}</tbody></table>}{!loading&&rows.length===0&&<Empty/>}</div></div></>
}
