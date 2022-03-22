<script>
    import bulmaCalendar from "~bulma-calendar/dist/js/bulma-calendar.min.js";
    import { username, role, jwtToken } from "../../Stores/User";
    import { onMount } from "svelte";
    import Api from "../../Api/Api";
    import { SavingStatus } from "../../Enums/SavingStatus";

    onMount(async () => {
        calendar = bulmaCalendar.attach('[type="date"]', {
            type: "datetime",
            displayMode: "inline",
        })[0];
        calendar.on('select', e => (console.log(e) || null));
        getAvailableLiturgien();
    });

    async function getAvailableLiturgien() {
        Api.Liturgie.get()
            .then((response) => {
                liturgien = response.data.liturgies;
            })
            .catch((error) => {
                console.log(error);
            });
    }

    function forceDeleteLiturgie() {
        removeLiturgie(selectedLiturgie, true);
    }

    async function removeLiturgie(liturgie, forceDelete) {
        selectedLiturgie = liturgie;
        deletingStatus = SavingStatus.SAVING;
        console.log($jwtToken);
        isForceDelete = false;
        Api.Liturgie.delete(liturgie.uuid, forceDelete, $jwtToken)
            .then((response) => {
                isForceDelete = false;
                deletingStatus = SavingStatus.SUCCESS;
                console.log(response);
            })
            .catch((error) => {
                isForceDelete = true;
                deletingStatus = SavingStatus.FAILED;
                console.log(error);
            })
            .finally(() => {
                getAvailableLiturgien();
                deselectLiturgie();
            });
    }

    let liturgien = [];
    let selectedLiturgie = null;
    let isEditing = false;
    let maxPeopleCountChurch = 15;
    let maxPeopleCountSala = 15;
    let liturgieDescription = "Messe";
    let liturgieDate = null;
    let savingStatus = SavingStatus.NONE;
    let deletingStatus = SavingStatus.NONE;
    let calendar = null;
    let isForceDelete = false;

    function deselectLiturgie() {
        isEditing = false;
        isForceDelete = false;
        liturgieDate = null;
        savingStatus = SavingStatus.NONE;
        deletingStatus = SavingStatus.NONE;
        maxPeopleCountChurch = 15;
        maxPeopleCountSala = 15;
        liturgieDescription = "Messe";
        selectedLiturgie = null;
    }

    function updateLiturgie() {
        savingStatus = SavingStatus.SAVING;
        const data = {
            date: new Date(calendar.value()).toISOString(),
            maxChurchCount: maxPeopleCountChurch,
            maxSalaCount: maxPeopleCountSala,
            uuid: selectedLiturgie ? selectedLiturgie.uuid : "",
            description: liturgieDescription
        };
        Api.Liturgie.putAuthenticated(data, $jwtToken)
            .then(() => {
                savingStatus = SavingStatus.SUCCESS;
            })
            .catch((error) => {
                savingStatus = SavingStatus.FAILED;
                console.error(error);
            })
            .finally(() => {
                getAvailableLiturgien();
                deselectLiturgie();
            });
    }

    function selectLiturgie(liturgie) {
        selectedLiturgie = liturgie;
        if(liturgie){
            maxPeopleCountChurch = liturgie.maxChurchCount;
            maxPeopleCountSala = liturgie.maxSalaCount;
            liturgieDate = liturgie.date;
            liturgieDescription = liturgie.description;
        }
        isEditing = true;
    }
</script>

<div>
    <button
        class="button is-success is-outlined"
        on:click={() => selectLiturgie(null)}
    >
        <span class="icon is-small">
            <i class="fas fa-plus" />
        </span>
    </button>
    <div class="table-container">
        <table class="table is-striped is-hoverable">
            <thead>
                <tr>
                    <th>Liturgie</th>
                    <th>Datum</th>
                    <th>Kirche maximal reservierte Plätze</th>
                    <th>Sala maximal reservierte Plätze</th>
                    <th>Aktionen</th>
                </tr>
            </thead>
            <tbody>
                {#each liturgien as liturgie (liturgie.uuid)}
                    <tr>
                        <td>{liturgie.description}</td>
                        <td>{new Date(liturgie.date).toLocaleString()}</td>
                        <td>{liturgie.maxChurchCount}</td>
                        <td>{liturgie.maxSalaCount}</td>
                        <td>
                            <button
                                class="button is-link is-outlined"
                                on:click={() => selectLiturgie(liturgie)}
                                ><span class="icon is-small">
                                    <i class="fas fa-edit" />
                                </span></button
                            >

                            <button
                                class="button is-danger is-outlined"
                                on:click={() => removeLiturgie(liturgie, false)}
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
</div>

<div id="liturgie-delete-modal" class="modal {isForceDelete && 'is-active'}">
    <div class="modal-background" on:click={deselectLiturgie} />
    <div class="modal-content">
        <div class="card">
            <header class="card-header">
                <p class="card-header-title">
                    Liturgie Hinzufügung/Aktualisierung
                </p>
            </header>
            <div class="card-content">
                <p>
                    Liturgie have registrations! <br /> Are you sure you want to
                    delete it and all the already made reservations?
                </p>

                <div class="field is-grouped is-grouped-right">
                    <div class="control">
                        <button
                            class="button is-danger {deletingStatus ==
                                SavingStatus.SAVING && 'is-loading'}"
                            on:click={forceDeleteLiturgie}>Delete</button
                        >
                    </div>
                    <div class="control">
                        <button
                            class="button is-link is-light"
                            on:click={deselectLiturgie}>Cancel</button
                        >
                    </div>
                </div>
            </div>
        </div>
    </div>
    <button
        class="modal-close is-large"
        aria-label="close"
        on:click={deselectLiturgie}
    />
</div>

<div id="liturgie-modal" class="modal {isEditing && 'is-active'}">
    <div class="modal-background" on:click={deselectLiturgie} />
    <div class="modal-content">
        <div class="card">
            <header class="card-header">
                <p class="card-header-title">
                    Liturgie Hinzufügung/Aktualisierung
                </p>
            </header>
            <div class="card-content">
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Liturgie Name</label
                    >
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    class="input"
                                    type="text"
                                    placeholder="Messe"
                                    bind:value={liturgieDescription}
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label">Datum</label>
                    <div class="field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    id="liturgie-date"
                                    class="input"
                                    type="date"
                                    placeholder="Name"
                                    bind:value={liturgieDate}
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Maximal Leute Zahl Kirche</label
                    >
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    class="input"
                                    type="number"
                                    placeholder="1"
                                    bind:value={maxPeopleCountChurch}
                                    min="1"
                                    step="1"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Maximal Leute Zahl Saal</label
                    >
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    class="input"
                                    type="number"
                                    placeholder="1"
                                    bind:value={maxPeopleCountSala}
                                    min="1"
                                    step="1"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field is-grouped is-grouped-right">
                    <div class="control">
                        <button
                            class="button is-link {savingStatus ==
                                SavingStatus.SAVING && 'is-loading'}"
                            on:click={updateLiturgie}>Save</button
                        >
                    </div>
                    <div class="control">
                        <button
                            class="button is-link is-light"
                            on:click={deselectLiturgie}>Cancel</button
                        >
                    </div>
                </div>
            </div>
        </div>
    </div>
    <button
        class="modal-close is-large"
        aria-label="close"
        on:click={deselectLiturgie}
    />
</div>
