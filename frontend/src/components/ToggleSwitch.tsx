type Props = {
  checked: boolean;
  onChange: () => void;
};

export default function ToggleSwitch({ checked, onChange }: Props) {
  return (
    <button
      onClick={onChange}
      className={`w-14 h-8 flex items-center rounded-full p-1 transition-colors
        ${checked ? "bg-green-500" : "bg-gray-400"}`}
    >
      <div
        className={`bg-white w-6 h-6 rounded-full shadow-md transform transition-transform
          ${checked ? "translate-x-6" : "translate-x-0"}`}
      />
    </button>
  );
}
