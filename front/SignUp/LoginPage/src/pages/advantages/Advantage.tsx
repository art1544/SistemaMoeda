import Navbar from "../../components/navbar/Navbar"
import Rewards from "../../components/rewards/Rewards"

const Advantage: React.FC = () =>{
    return(
        <div>
             <div className="home-container with-navbar">
                <Navbar/>
             </div>
             <div>
                <Rewards/>
             </div>

        </div>
    )
}
export default Advantage