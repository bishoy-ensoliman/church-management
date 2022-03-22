<script>
    import Api from "../../Api/Api";
import { SavingStatus } from "../../Enums/SavingStatus";

    let firstName = "";
    let lastName = "";
    let password = "";
    let isPasswordValid = true;
    let reservations = [];
    let isSearchClicked = false;
    let isDeleting = false;
    let deletingStatus = SavingStatus.NONE;
    let selectedReservation = null;

    function deselectReservation() {
        isDeleting = false;
        isPasswordValid = true;
        deletingStatus = SavingStatus.NONE; 
        password = "";
        selectedReservation = null;
    }

    function selectReservation(reservation) {
        isDeleting = true;
        selectedReservation = reservation;
    }

    async function deleteReservation() {
        isPasswordValid = password;
        if(!isPasswordValid){
            return;
        }
        Api.Reservation.delete(selectedReservation.uuid, password)
            .then((response) => {
                console.log(response);
            })
            .catch((error) => {
                console.log(error);
            })
            .finally(() => {
                getReservations();
                deselectReservation();
            });
    }

    async function getReservations() {
        isSearchClicked = false;
        if (firstName && lastName) {
            Api.Reservation.get(firstName, lastName)
                .then((response) => {
                    reservations = response.data.reservations;
                })
                .catch((error) => {
                    console.log(error);
                }).finally(() => {
                    isSearchClicked = true;
                });
        } else {
            reservations = [];
            isSearchClicked = true;
        }
    }
</script>

<h1 class="title center-horz">Meine Reservierungen</h1>
<div class="columns">
    <div class="box column is-8 is-offset-2">
        <a class="button is-info" href="/liturgie-registration">
            Neue Reservierung</a
        >
        <br/>
        <br/>
        <div class="field has-addons center">
            <div class="control">
                <input
                    class="input"
                    type="text"
                    placeholder="Vorname"
                    bind:value={firstName}
                />
            </div>
            <div class="control">
                <input
                    class="input"
                    type="text"
                    placeholder="Name"
                    bind:value={lastName}
                />
            </div>
            <div class="control">
                <button class="button is-info" on:click={getReservations}>
                    Search
                </button>
            </div>
        </div>

        {#if reservations.length > 0}
            <div class="table-container">
                <table class="table is-striped is-hoverable center">
                    <thead>
                        <tr>
                            <th>Liturgie</th>
                            <th>Ort</th>
                            <th>Zahl</th>
                            <th>Aktionen</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each reservations as reservation (reservation.uuid)}
                            <tr>
                                <td>{new Date(reservation.liturgieDate).toLocaleString()}</td>
                                <td>{reservation.place}</td>
                                <td>{reservation.numberOfPeople}</td>
                                <td>
                                    <button
                                        class="button is-danger is-outlined"
                                        on:click={() => selectReservation(reservation)}
                                        ><span class="icon is-small">
                                            <i class="fas fa-times" />
                                        </span></button
                                    >
                                </td>
                            </tr>
                        {/each}
                    </tbody>
                </table>
            </div>
        {:else if isSearchClicked}
            <span class="red">keine Ergebnisse</span>
        {/if}
    </div>
</div>

<div id="booking-modal" class="modal {isDeleting && 'is-active'}">
    <div class="modal-background" on:click={deselectReservation} />
    <div class="modal-content">
        <div class="card">
            <header class="card-header">
                <p class="card-header-title">Registrierung Stornierung</p>
            </header>
            <div class="card-content">
                <div class="field is-horizontal">
                    <label class="label field-label">Pin <span class="red">*</span></label>
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    bind:value={password}
                                    class="input {!isPasswordValid && 'is-danger'}"
                                    type="text"
                                    placeholder="PIN"
                                />
                                {#if !isPasswordValid}
                                    <span class="icon is-small is-right">
                                        <i
                                            class="fas fa-exclamation-triangle"
                                        />
                                    </span>
                                {/if}
                            </div>
                            {#if !isPasswordValid}
                                <p class="help is-danger">
                                    Bitte geben Sie eine PIN!
                                </p>
                            {/if}
                        </div>
                    </div>
                </div>
                
                <div class="field is-grouped is-grouped-right">
                    <div class="control">
                        <button
                            class="button is-link {deletingStatus ==
                                SavingStatus.BOOKING && 'is-loading'}"
                            on:click={deleteReservation}>Delete</button
                        >
                    </div>
                    <div class="control">
                        <button
                            class="button is-link is-light"
                            on:click={deselectReservation}>Cancel</button
                        >
                    </div>
                </div>
            </div>
        </div>
    </div>
    <button
        class="modal-close is-large"
        aria-label="close"
        on:click={deselectReservation}
    />
</div>

<style>
    
</style>
