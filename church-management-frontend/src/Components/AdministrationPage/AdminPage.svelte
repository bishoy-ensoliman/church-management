<script>
    import { username, role, jwtToken } from "../../Stores/User";
    import LiturgieTab from "./LiturgieTab.svelte";
    import ReservationTab from "./ReservationTab.svelte";

    const TabNames = Object.freeze({
        LITURGIE: "LITURGIE",
        RESERVATION: "RESERVATION",
        USERS: "USERS",
    });

    let activeTab = TabNames.LITURGIE;

    function setActiveTab(activeTabName) {
        activeTab = activeTabName;
    }
</script>

{#if $jwtToken && $role == "ROLE_ADMIN"}
    <h1 class="title level-item">Admin</h1>
    <div class="columns">
        <div class="column box is-10 center">
            <div class="tabs is-boxed">
                <ul>
                    <li
                        class={activeTab == TabNames.LITURGIE && "is-active"}
                        on:click={() => {
                            setActiveTab(TabNames.LITURGIE);
                        }}
                    >
                        <a>
                            <span class="is-small">
                                <span>Liturgie</span>
                            </span></a
                        >
                    </li>
                    <li
                        class={activeTab == TabNames.RESERVATION && "is-active"}
                        on:click={() => {
                            setActiveTab(TabNames.RESERVATION);
                        }}
                    >
                        <a>
                            <span class="is-small">
                                <span>Reservirung</span>
                            </span></a
                        >
                    </li>
                    <li
                        class={activeTab == TabNames.USERS && "is-active"}
                        on:click={() => {
                            setActiveTab(TabNames.USERS);
                        }}
                    >
                        <a>
                            <span class="is-small">
                                <span>Benutzer</span>
                            </span></a
                        >
                    </li>
                </ul>
            </div>
            {#if activeTab == TabNames.LITURGIE}
                <LiturgieTab />
            {/if}
            {#if activeTab == TabNames.RESERVATION}
                <ReservationTab />
            {/if}
        </div>
    </div>
{:else}
    <h1 class="title level-item red">Unauthorized!</h1>
{/if}

<style>
    .red {
        color: red;
    }
    .center {
        margin: 0 auto;
    }
</style>
