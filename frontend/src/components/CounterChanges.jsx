import React from "react";
import { PiPlusMinusFill } from "react-icons/pi";
import { FaSquarePlus, FaSquareMinus } from "react-icons/fa6";


export default function CounterChanges({ additions = 0, deletions = 0 }) {

    const changeStyle = { display: "flex", alignItems: "center" }
    const iconStyle = { fontSize: 30, marginRight: 5 }
    const numberStyle = { fontSize: 20, marginTop: 15 }
    return (<div style={{ display: "flex", justifyContent: "space-between"}}>
        {
            additions > 0 || deletions > 0 ? <>
                <div style={changeStyle}>{additions > 0 && <>
                    <FaSquarePlus style={iconStyle} color="green" />
                    <p style={numberStyle}>
                        {additions}
                    </p>
                </>}</div>
                <div style={changeStyle}>{deletions > 0 && <>
                    <FaSquareMinus style={iconStyle} color="red" />
                    <p style={numberStyle}>
                        {deletions}
                    </p>
                </>
                }</div>
                <div style={changeStyle}>{additions > 0 && deletions > 0 && <>
                    <PiPlusMinusFill style={iconStyle} color="orange" />
                    <p style={numberStyle}>
                        {additions + deletions}
                    </p>
                </>
                }</div></> : "SIN CAMBIOS"
}


    </div >);
}