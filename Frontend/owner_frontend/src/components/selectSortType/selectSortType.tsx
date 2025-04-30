

//@ts-ignore
function SelectSortType({options, defaultOption, value, onChange }) {
    return (<select
            value={value}
            onChange={event => onChange(event.target.value)}
    >
        <option value="isPinned">{defaultOption}</option>
        {/*//@ts-ignore*/}
        {options.map(option => (
            <option key={option.key} value={option.key}>{option.value}</option>
        ))}
    </select>)
}

export default SelectSortType;