// GET /api/gardens

export interface Garden {
  id: number;
  name: string;
}

const gardens: Garden[] = [
    {
        id: 1,
        name: "Vườn A"
    },
    {
        id: 2,
        name: "Vườn B"
    }
];

export default gardens;