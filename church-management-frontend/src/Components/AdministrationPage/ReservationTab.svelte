<script>
    import { username, role, jwtToken } from "../../Stores/User";
    import { onMount } from "svelte";
    import Api from "../../Api/Api";

    let reservations = [];

    onMount(async () => {
        getReservations();
    });

    async function getReservations() {
        Api.Reservation.getAuthenticated($jwtToken)
            .then((response) => {
                reservations = response.data.reservations;
                console.log(reservations);
            })
            .catch((error) => {
                console.log(error);
            });
    }
</script>

<div class="table-container">
    <table class="table is-striped is-hoverable">
        <thead>
            <tr>
                <th>Registration Datum</th>
                <th>Name</th>
                <th>Vorname</th>
                <th>Email</th>
                <th>Mobile</th>
                <th>Zahl</th>
                <th>Ort</th>
                <th>Approved</th>
                <th>Litergie Datum</th>
            </tr>
        </thead>
        <tbody>
            {#each reservations as reservation (reservation.uuid)}
                <tr>
                    <td
                        >{new Date(
                            reservation.registrationDate
                        ).toLocaleString()}</td
                    >
                    <td>{reservation.lastName}</td>
                    <td>{reservation.firstName}</td>
                    <td>{reservation.email}</td>
                    <td>{reservation.mobile}</td>
                    <td>{reservation.numberOfPeople}</td>
                    <td>{reservation.place}</td>
                    <td>{reservation.approved}</td>
                    <td
                        >{new Date(
                            reservation.liturgieDate
                        ).toLocaleString()}</td
                    >
                </tr>
            {/each}
        </tbody>
    </table>
</div>
