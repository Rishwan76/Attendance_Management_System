export function PageHeader({title,subtitle,children}) {
  return <div className="page-header"><div><h1>{title}</h1>{subtitle&&<p>{subtitle}</p>}</div><div className="header-actions">{children}</div></div>
}
export function StatCard({title,value,icon:Icon,detail}) {
  return <div className="stat-card"><div className="stat-icon"><Icon size={22}/></div><div><span>{title}</span><strong>{value}</strong>{detail&&<small>{detail}</small>}</div></div>
}
export function Loading(){return <div className="loading">Loading...</div>}
export function Empty({text='No records found'}){return <div className="empty">{text}</div>}
export function ErrorBox({error}){return error?<div className="error-box">{error}</div>:null}
