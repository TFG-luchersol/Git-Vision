import React from "react";
import { RiMedalLine } from "react-icons/ri";
import { Input, Table } from "reactstrap";

export default function Ranking({ranking, typeRanking, setTypeRanking}) {
  const GoldMedal = <RiMedalLine className="medal gold" />;
  const SilverMedal = <RiMedalLine className="medal silver" />;
  const BronzeMedal = <RiMedalLine className="medal bronze" />;
  const EmptyMedal = <RiMedalLine className="medal dotted" />;

  return (
    <>
      <div style={{ display: "flex", justifyContent: "center" }}>
        <p>Ranking</p>
      </div>
      <div style={{ height: "50vh", overflowY: "auto", marginInline: 10 }}>
        <Table striped bordered responsive>
          <thead>
            <tr>
              <th>Medalla</th>
              <th>Nombre</th>
              <th>Puntos</th>
            </tr>
          </thead>
          <tbody>
            {ranking.map((user, index) => (
              <tr key={index}>
                <td>
                  {index === 0
                    ? GoldMedal
                    : index === 1
                    ? SilverMedal
                    : index === 2
                    ? BronzeMedal
                    : EmptyMedal}
                </td>
                <td>{user.name}</td>
                <td>{user.points}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          marginTop: 10,
          marginLeft: 10,
        }}
      >
        <p>PORCENTAJE:</p>
        <Input
          checked={typeRanking}
          style={{ marginLeft: 10 }}
          type="checkbox"
          onChange={() => setTypeRanking((prev) => !prev)}
        />
      </div>
    </>
  );
}
