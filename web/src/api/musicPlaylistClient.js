import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class MusicPlaylistClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getBooklist', 'getBooklistBooks', 'createBooklist', 'search', 'deleteBooklist'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Gets the booklist for the given ID.
     * @param id Unique identifier for a booklist
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The booklist's metadata.
     */
    async getBooklist(id, errorCallback) {
        try {
            const response = await this.axiosClient.get(`booklists/${id}`);
            return response.data.booklist;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Get the books on a given booklist by the booklist's identifier.
     * @param id Unique identifier for a booklist
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The list of books on a booklist.
     */
    async getBooklistBooks(id, errorCallback) {
        try {
            const response = await this.axiosClient.get(`booklists/${id}/books`);
            return response.data.books;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Create a new booklist owned by the current user.
     * @param name The name of the booklist to create.
     * @param tags Metadata tags to associate with a booklist.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The booklist that has been created.
     */
    async createBooklist(name, tags, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create booklists.");
            const response = await this.axiosClient.post(`booklists`, {
                name: name,
                tags: tags,
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.booklist;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Add a book to a booklist.
     * @param id The id of the booklist to add a book to.
     * @param asin The asin that uniquely identifies the book.
     * @returns The list of books on a booklist.
     */
    async addBookToBooklist(id, asin, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can add a song to a playlist.");
            const response = await this.axiosClient.post(`booklists/${id}/books`, {
                id: id,
                asin: asin
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.books;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Search for a book.
     * @param criteria A string containing search criteria to pass to the API.
     * @returns The booklists that match the search criteria.
     */
    async search(criteria, errorCallback) {
        try {
            const queryParams = new URLSearchParams({ q: criteria })
            const queryString = queryParams.toString();

            const response = await this.axiosClient.get(`booklists/search?${queryString}`);

            return response.data.booklists;
        } catch (error) {
            this.handleError(error, errorCallback)
        }

    }

    /**
         * Gets the booklist for the given ID.
         * @param id Unique identifier for a booklist
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The booklist's metadata.
         */
        async deleteBooklist(id, errorCallback) {
            try {
                const response = await this.axiosClient.delete(`booklists/${id}`);
                return response.data.booklist;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}
