import './styles/switch.css'

//@ts-ignore
function CustomSwitch({checked, setChecked}) {
  return <label className="switch">
    <input type="checkbox" value={checked} onChange={() => setChecked(!checked)} checked={checked} />
    <span className="slider"></span>
  </label>
}

export default CustomSwitch;
