export default function ListeMaladies(props) {
var diseases = props.diseases;
var selected = props.selected;
var onCheck = props.onCheck;
 return (
  <div style={{ border: "1px solid #ccc", padding: "15px", height: "250px", overflowY: "scroll" }}>
  {diseases.map((d) => (
  <div key={d.id} 
    style={{ marginBottom: "8px" }}>
  <label>
 <input
 type="checkbox"
checked={selected.includes(d.id)}
onChange={() => onCheck(d.id)}
/>
{" "}{d.nom}
</label>
</div>
))}
</div>
);
}