import "./styles/tagsEdit.css"

interface ITag {
    name: string;
    description: string;
}

function TagWithDescriptionButton({tag, onClick, className} :{tag: ITag; onClick: () => void; className?:string}) {
    return (
        <button onClick={onClick} className={`${className} text-left border border-gray-100 shadow p-4 edit_tags mt-2`}>
            <h3>{tag.name}</h3>
            <p >{tag.description}</p>
        </button>
    )
}

export default TagWithDescriptionButton;