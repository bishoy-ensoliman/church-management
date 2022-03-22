<script>
	import { Route, router, active } from "tinro";
	import AdminPage from "./Components/AdministrationPage/AdminPage.svelte";
	import LiturgieRegistration from "./Components/LiturgieRegistrationPage/LiturgieRegistration.svelte";
	import LoginPage from "./Components/LoginPage/LoginPage.svelte";
	import PostsPage from "./Components/PostsPage/PostsPage.svelte";
	import RegisterUser from "./Components/RegisterUserPage/RegisterUser.svelte";
	import UserLiturgieRegistration from "./Components/UserLiturgieRegistrationPage/UserLiturgieRegistration.svelte";
	import { username, role, jwtToken } from "./Stores/User";

	const frontendURL = process.env.isProd
		? "frontendProdUrl"
		: "frontendDevUrl";

	let isMenuVisible = false;

	function logout() {
		// Todo invalidate the jwt token on server
		$jwtToken = "";
		$role = "";
		$username = "";
		window.localStorage.removeItem("user");
		router.goto("/");
	}
</script>

<nav class="navbar is-dark" role="navigation" aria-label="main navigation">
	<div class="navbar-brand">
		<a class="navbar-item home-nav" href="/">
			<img src="stmina-32x32.png" />
			<span class="home-link">Home</span>
		</a>
		<a
			role="button"
			class="navbar-burger"
			data-target="navMenu"
			aria-label="menu"
			aria-expanded="false"
			on:click={() => {
				isMenuVisible = !isMenuVisible;
			}}
		>
			<span aria-hidden="true" />
			<span aria-hidden="true" />
			<span aria-hidden="true" />
		</a>
	</div>

	<div class="navbar-menu {isMenuVisible && 'is-active'}">
		<div class="navbar-start">
			<!-- <a href="/benutzer-liturgie" class="navbar-item">
				Meine Reservierungen
			</a> -->

			<!-- <a class="navbar-item" href="/newsfeed">Neuigkeiten</a> -->
			<a href="/liturgie-registration" class="navbar-item">
				Kirchenliturgie Registrierung
			</a>
			<a class="navbar-item" href="/live-stream">Live Stream</a>
			{#if $role == "ROLE_ADMIN" && $jwtToken}
				<a class="navbar-item" href="/admin"> Admin </a>
			{/if}
			<!-- <div class="navbar-item has-dropdown is-hoverable">
				<a class="navbar-link"> Mehr </a>
				<div class="navbar-dropdown">
					<a class="navbar-item" href="/live-stream"> Live Stream </a>
					{#if $role == "ROLE_ADMIN" && $jwtToken}
						<hr class="navbar-divider" />
						<a class="navbar-item" href="/admin"> Admin </a>
					{/if}
				</div>
			</div> -->
		</div>

		<div class="navbar-end">
			<div class="navbar-item">
				<div>
					{#if !$jwtToken}
						<!-- <a href="/sign-up" class="button is-primary">
							<strong>Registrieren</strong>
						</a> -->
						<a href="/login" class="button is-light">Anmelden</a>
					{:else}
						<div class="navbar-item has-dropdown is-hoverable">
							<a class="navbar-item user-icon">
								<span class="icon is-small">
									<i class="fas fa-user" />
								</span>
							</a>

							<div class="navbar-dropdown is-right">
								<a class="navbar-item" on:click={logout}
									>Abmelden</a
								>
							</div>
						</div>
					{/if}
				</div>
			</div>
		</div>
	</div>
</nav>

<div class="document-body">
	<Route path="/">
		<h1 class="title center-horz">Willkommen zu St. Mina Kirche München</h1>
		<div class="columns">
			<div class="column box is-10 is-offset-1 center-horz">
				<img src="stminakirche.jpeg" />
			</div>
		</div>
	</Route>
	<Route path="/liturgie-registration"><LiturgieRegistration /></Route>
	<Route path="/login"><LoginPage /></Route>
	<Route path="/newsfeed"><PostsPage /></Route>
	<Route path="/benutzer-liturgie"><UserLiturgieRegistration /></Route>
	<Route path="/sign-up"><RegisterUser /></Route>
	<Route path="/live-stream">
		<h1 class="title center-horz">Live Stream</h1>
		<div class="columns">
			<div class="column box is-10 is-offset-1">
				<div class="container">
					<iframe
						class="responsive-iframe"
						id="live-video"
						src={"https://www.youtube.com/embed/live_stream?channel=UCQm5Tny5xNeitKsJrxOAxzQ&enablejsapi=1&version=3&origin=" +
							frontendURL}
						frameborder="0"
						allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
						allowfullscreen
						enablejsapi="1"
					/>
				</div>
			</div>
		</div>
	</Route>
	<Route path="/admin"><AdminPage /></Route>
</div>

<style>
	.center-horz {
		text-align: center;
	}
	.home-link {
		margin-left: 10px;
	}

	.container {
		position: relative;
		overflow: hidden;
		width: 100%;
		padding-top: 56.25%; /* 16:9 Aspect Ratio (divide 9 by 16 = 0.5625) */
	}

	/* Then style the iframe to fit in the container div with full height and width */
	.responsive-iframe {
		position: absolute;
		top: 0;
		left: 0;
		bottom: 0;
		right: 0;
		width: 100%;
		height: 100%;
	}

	.document-body {
		padding-top: 1rem;
		padding-bottom: 3rem;
		padding-left: 1rem;
		padding-right: 1rem;
	}

	.user-icon {
    	color: #fff;
	}

	.user-icon:hover {
    	background-color: #292929;
		color: #fff;
	} 

	.is-active .user-icon:hover {
    	background-color: #fafafa;
		color: #3273dc;
	}

	.home-nav:hover {
		background-color: #292929;
	}
	.is-active  .user-icon{
		color: #4a4a4a;
	}
</style>
