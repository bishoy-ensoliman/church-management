<script>
    import Api from "../../Api/Api";
    import { onMount } from "svelte";
    import { BookingStatus } from "../../Enums/BookingStatus";

    onMount(async () => {
        getAvailableLiturgien();
    });

    let liturgien = [];

    let isBooking = false;
    let bookingStatus = BookingStatus.NONE;

    let isEmailValid = true;
    let isPasswordValid = true;
    let isLastNameValid = true;
    let isFirstNameValid = true;
    let isPeopleCoundValid = true;
    let isPlaceValid = true;

    let maxNumberOfPeople = 0;

    let userLastName = "";
    let password = "";
    let userFirstName = "";
    let userEmail = "";
    let mobile = "";
    let selectedPlace = "PLEASE_SELECT";
    let selectedLiturgie = null;
    let selectedNumberOfPeople = 1;

    function selectLiturgie(liturgie) {
        isBooking = true;
        selectedLiturgie = liturgie;
    }

    function deselectLiturgie() {
        isBooking = false;

        isEmailValid = true;
        isPasswordValid = true;
        isFirstNameValid = true;
        isLastNameValid = true;
        isPeopleCoundValid = true;
        isPlaceValid = true;

        maxNumberOfPeople = 0;

        userLastName = "";
        userFirstName = "";
        password = "";
        userEmail = "";
        mobile = "";
        selectedPlace = "PLEASE_SELECT";
        selectedLiturgie = null;
        selectedNumberOfPeople = 1;
    }

    function changeMaxNumberOfPeople() {
        maxNumberOfPeople = 0;

        if (selectedLiturgie && selectedPlace == "KIRCHE")
            maxNumberOfPeople =
                selectedLiturgie.maxChurchCount - selectedLiturgie.churchCount;

        if (selectedLiturgie && selectedPlace == "SAAL")
            maxNumberOfPeople =
                selectedLiturgie.maxSalaCount - selectedLiturgie.salaCount;
    }

    function validateEmail(email) {
        const re =
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(email).toLowerCase());
    }

    function createRegistration() {
        isEmailValid = userEmail == "" || validateEmail(userEmail);
        isLastNameValid = userLastName;
        isPasswordValid = password;
        isFirstNameValid = userFirstName;
        isPlaceValid = selectedPlace === "KIRCHE" || selectedPlace === "SAAL";
        console.log(selectedNumberOfPeople);
        console.log(selectedLiturgie);
        isPeopleCoundValid =
            (selectedPlace === "KIRCHE" &&
                selectedNumberOfPeople + selectedLiturgie.churchCount <=
                    selectedLiturgie.maxChurchCount) ||
            (selectedPlace === "SAAL" &&
                selectedNumberOfPeople + selectedLiturgie.salaCount <=
                    selectedLiturgie.maxSalaCount);

        if (
            !isPlaceValid ||
            !isEmailValid ||
            !isLastNameValid ||
            !isPeopleCoundValid ||
            !isFirstNameValid ||
            !isPasswordValid
        )
            return;

        sendRegistrationDataToServer();
    }

    async function sendRegistrationDataToServer() {
        bookingStatus = BookingStatus.BOOKING;
        const data = {
            email: userEmail,
            liturgieUUID: selectedLiturgie.uuid,
            lastName: userLastName,
            firstName: userFirstName,
            mobile: mobile,
            numberOfPeople: selectedNumberOfPeople,
            place: selectedPlace,
            password: password,
        };
        Api.Reservation.put(data)
            .then((response) => {
                const reservationStatus = response.data.approved;
                bookingStatus = reservationStatus
                    ? BookingStatus.SUCCESS
                    : BookingStatus.FAILED;
            })
            .catch((error) => {
                bookingStatus = BookingStatus.FAILED;
                console.error(error);
            })
            .finally(() => {
                setTimeout(closeNotification, 3000);
                getAvailableLiturgien();
                deselectLiturgie();
            });
    }

    async function getAvailableLiturgien() {
        Api.Liturgie.get()
            .then((response) => {
                liturgien = response.data.liturgies;
            })
            .catch((error) => {
                console.log(error);
            });
    }

    function closeNotification() {
        bookingStatus = BookingStatus.NONE;
    }
</script>

<div class="columns level">
    <div class="center">
        <div class="column">
            <h1 class="title level-item">Liturgie Registrierung</h1>
            {#if bookingStatus == BookingStatus.SUCCESS}
                <div class="notification is-primary">
                    <button class="delete" on:click={closeNotification} />
                    Buchung Erfolgt!
                </div>
            {:else if bookingStatus == BookingStatus.FAILED}
                <div
                    class="notification is-danger"
                    on:click={closeNotification}
                >
                    <button class="delete" />
                    Buchung Fählt!
                </div>
            {/if}
            <a class="button is-info float-right" href="/benutzer-liturgie">
                Meine Reservierungen</a
            >
            <br />
            <br />
            <p class="red">
                * Bitte beachten Sie, dass die Mindestzeit zwischen den
                Anmeldungen für Liturgien 2 Wochen beträgt
            </p>
            <p class="red" dir="rtl">
                * يرجى ملاحظة أن الحد الأدنى للوقت بين تسجيلات الصلوات هو
                أسبوعين
            </p>
            <div class="box">
                <h2 class="title is-4 level-item">Verfügbare Liturgie</h2>
                <div class="table-container">
                    <table class="table is-striped is-hoverable">
                        <thead>
                            <tr>
                                <th>Liturgie</th>
                                <th>Datum</th>
                                <th>Verfügbare Kirche Sitze</th>
                                <th>Verfügbare Saal Sitze</th>
                                <th>Buchen</th>
                            </tr>
                        </thead>
                        <tbody>
                            {#each liturgien as liturgie (liturgie.uuid)}
                                <tr>
                                    <td>{liturgie.description}</td>
                                    <td
                                        >{new Date(
                                            liturgie.date
                                        ).toLocaleString()}</td
                                    >
                                    <td
                                        >{liturgie.maxChurchCount -
                                            liturgie.churchCount}</td
                                    >
                                    <td
                                        >{liturgie.maxSalaCount -
                                            liturgie.salaCount}</td
                                    >
                                    <td>
                                        {#if liturgie.maxChurchCount + liturgie.maxSalaCount <= liturgie.churchCount + liturgie.salaCount}
                                            <button
                                                class="button is-danger"
                                                title="Disabled button"
                                                disabled>AUSGEBUCHT</button
                                            >
                                        {:else}
                                            <button
                                                class="button is-primary"
                                                on:click={() =>
                                                    selectLiturgie(liturgie)}
                                                >Online Buchen</button
                                            >
                                        {/if}
                                    </td>
                                </tr>
                            {/each}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="booking-modal" class="modal {isBooking && 'is-active'}">
    <div class="modal-background" on:click={deselectLiturgie} />
    <div class="modal-content">
        <div class="card">
            <header class="card-header">
                <p class="card-header-title">Liturgie Buchung</p>
            </header>
            <div class="card-content">
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Name
                        <span class="red">*</span>
                    </label>
                    <div class="field-body">
                        <div class="field">
                            <div class="control is-expanded has-icons-right">
                                <input
                                    class="input {!isFirstNameValid &&
                                        'is-danger'}"
                                    type="text"
                                    placeholder="Vorname"
                                    bind:value={userFirstName}
                                />
                                {#if !isFirstNameValid}
                                    <span class="icon is-small is-right">
                                        <i
                                            class="fas fa-exclamation-triangle"
                                        />
                                    </span>
                                {/if}
                            </div>
                            {#if !isFirstNameValid}
                                <p class="help is-danger">
                                    Bitte geben Sie Ihren Vornamen ein!
                                </p>
                            {/if}
                        </div>

                        <div class="field">
                            <div class="control is-expanded has-icons-right">
                                <input
                                    class="input {!isLastNameValid &&
                                        'is-danger'}"
                                    type="text"
                                    placeholder="Name"
                                    bind:value={userLastName}
                                />
                                {#if !isLastNameValid}
                                    <span class="icon is-small is-right">
                                        <i
                                            class="fas fa-exclamation-triangle"
                                        />
                                    </span>
                                {/if}
                            </div>
                            {#if !isLastNameValid}
                                <p class="help is-danger">
                                    Bitte geben Sie Ihren Namen ein!
                                </p>
                            {/if}
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Pin <span class="red">*</span></label
                    >
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    bind:value={password}
                                    class="input {!isPasswordValid &&
                                        'is-danger'}"
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
                <div class="field is-horizontal">
                    <label class="label field-label">Email</label>
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-left has-icons-right">
                                <input
                                    bind:value={userEmail}
                                    class="input {!isEmailValid && 'is-danger'}"
                                    type="email"
                                    placeholder="Email"
                                />
                                <span class="icon is-small is-left">
                                    <i class="fas fa-envelope" />
                                </span>
                                {#if !isEmailValid}
                                    <span class="icon is-small is-right">
                                        <i
                                            class="fas fa-exclamation-triangle"
                                        />
                                    </span>
                                {/if}
                            </div>
                            {#if !isEmailValid}
                                <p class="help is-danger">
                                    Bitte geben Sie eine gültige E-Mail für die
                                    Benachrichtigung ein!
                                </p>
                            {/if}
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label">Mobile</label>
                    <div class=" field-body">
                        <div class="field">
                            <div class="control">
                                <input
                                    bind:value={mobile}
                                    class="input"
                                    type="tel"
                                    placeholder="+49 12345678900"
                                />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Ort
                        <span class="red">*</span>
                    </label>
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <div
                                    class="select {!isPlaceValid &&
                                        'is-danger'}"
                                >
                                    <select
                                        bind:value={selectedPlace}
                                        on:blur={() =>
                                            changeMaxNumberOfPeople()}
                                    >
                                        <option
                                            selected="selected"
                                            value="PLEASE_SELECT"
                                        >
                                            Bitte auswählen
                                        </option>
                                        <option
                                            value="KIRCHE"
                                            disabled={selectedLiturgie &&
                                                selectedLiturgie.churchCount >=
                                                    selectedLiturgie.maxChurchCount}
                                        >
                                            Kirche
                                        </option>
                                        <option
                                            value="SAAL"
                                            disabled={selectedLiturgie &&
                                                selectedLiturgie.salaCount >=
                                                    selectedLiturgie.maxSalaCount}
                                            >Saal</option
                                        >
                                    </select>
                                    {#if !isPlaceValid}
                                        <span class="icon is-small is-right">
                                            <i
                                                class="fas fa-exclamation-triangle"
                                            />
                                        </span>
                                    {/if}
                                </div>
                            </div>
                            {#if !isPlaceValid}
                                <p class="help is-danger">
                                    Bitte wählen Sie einen gültigen Ort!
                                </p>
                            {/if}
                        </div>
                    </div>
                </div>
                <div class="field is-horizontal">
                    <label class="label field-label"
                        >Leute Zahl
                        <span class="red">*</span>
                    </label>
                    <div class=" field-body">
                        <div class="field">
                            <div class="control has-icons-right">
                                <input
                                    class="input {!isPeopleCoundValid &&
                                        'is-danger'}"
                                    type="number"
                                    placeholder="1"
                                    bind:value={selectedNumberOfPeople}
                                    min="1"
                                    step="1"
                                    max={maxNumberOfPeople}
                                />
                                {#if !isPeopleCoundValid}
                                    <span class="icon is-small is-right">
                                        <i
                                            class="fas fa-exclamation-triangle"
                                        />
                                    </span>
                                {/if}
                            </div>
                            {#if !isPeopleCoundValid}
                                <p class="help is-danger">
                                    Es gibt nur {maxNumberOfPeople} Plätze frei!
                                </p>
                            {/if}
                        </div>
                    </div>
                </div>
                <div class="field is-grouped is-grouped-right">
                    <div class="control">
                        <button
                            class="button is-link {bookingStatus ==
                                BookingStatus.BOOKING && 'is-loading'}"
                            on:click={createRegistration}>Buchen</button
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

<style>
    .red {
        color: red;
    }
    .center {
        margin: 0 auto;
    }
    .number-field-width {
        width: 100px;
    }

    .button-submit {
        background-color: #485fc7;
        color: #fff;
    }

    .button-submit:hover {
        background-color: #3e56c4;
    }

    .button-cancel {
        background-color: #eff1fa;
        color: #3850b7;
    }

    .button-cancel:hover {
        background-color: #e6e9f7;
    }
</style>
