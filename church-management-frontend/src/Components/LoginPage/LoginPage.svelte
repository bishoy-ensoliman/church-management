<script>
    import Api from "../../Api/Api";
    import { LogInStatus } from "../../Enums/LogInStatus";
    import { username, role, jwtToken } from "../../Stores/User";
    import { router } from "tinro";

    let password = "";

    let isValidName = true;
    let isValidPassword = true;

    let loggingInStatus = LogInStatus.NONE;

    function login() {
        loggingInStatus = LogInStatus.LOGGING_IN;
        isValidName = $username ? true : false;
        isValidPassword = password ? true : false;
        Api.User.authenticate($username, password)
            .then((response) => {
                loggingInStatus = LogInStatus.SUCCESS;
                $username = response.data.username;
                $role = response.data.role;
                $jwtToken = response.data.jwttoken;
                window.localStorage.setItem("user", JSON.stringify({
                    role: $role,
                    username: $username,
                    token: $jwtToken,
                }));
                setTimeout(reset, 3000);
                router.goto("/");
            })
            .catch((error) => {
                console.log(error);
                loggingInStatus = LogInStatus.FAILED;
            });
    }

    function reset() {
        isValidName = true;
        isValidPassword = true;
    }

    function closeNotification() {
        loggingInStatus = LogInStatus.NONE;
    }
</script>

{#if $jwtToken && $role == "ROLE_ADMIN"}
    <h1 class="title level-item">Sie sind bereits angemeldet</h1>
{:else}
    <div class="columns level">
        <div class="level-item">
            <div class="column box is-5 ">
                <h1 class="title level-item">Anmeldung</h1>
                {#if loggingInStatus == LogInStatus.FAILED}
                    <div
                        class="notification is-danger"
                        on:click={closeNotification}
                    >
                        <button class="delete" />
                        Anmeldung Fählt!
                    </div>
                {/if}
                <div class="field">
                    <label class="label">Username</label>
                    <div class="control has-icons-right">
                        <input
                            class="input {!isValidName && 'is-danger'}"
                            type="text"
                            placeholder="Name"
                            bind:value={$username}
                        />
                        {#if !isValidName}
                            <span class="icon is-small is-right">
                                <i class="fas fa-exclamation-triangle" />
                            </span>
                        {/if}
                    </div>
                    {#if !isValidName}
                        <p class="help is-danger">
                            Bitte geben Sie Ihren Namen ein!
                        </p>
                    {/if}
                </div>
                <div class="field">
                    <label class="label">Passwort</label>
                    <div class="control has-icons-right">
                        <input
                            class="input {!isValidPassword && 'is-danger'}"
                            type="password"
                            placeholder="Password"
                            bind:value={password}
                        />
                        {#if !isValidPassword}
                            <span class="icon is-small is-right">
                                <i class="fas fa-exclamation-triangle" />
                            </span>
                        {/if}
                    </div>
                    {#if !isValidPassword}
                        <p class="help is-danger">
                            Bitte geben Sie Ihren Passwort ein!
                        </p>
                    {/if}
                </div>
                <div class="field is-grouped">
                    <div class="control">
                        <button
                            class="button is-link {loggingInStatus ==
                                LogInStatus.LOGGING_IN && 'is-loading'}"
                            on:click={login}>Anmelden</button
                        >
                    </div>
                </div>
            </div>
        </div>
    </div>
{/if}

<style>
</style>
