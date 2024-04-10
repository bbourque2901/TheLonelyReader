import BookTrackerClient from '../api/bookTrackerClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";


const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


/**
 * Logic needed for the view booklist page of the website.
 */
class SearchBooklists extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'search', 'displaySearchResults', 'getHTMLForSearchResults', 'remove'], this);

        // Create a enw datastore with an initial "empty" state.
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
        console.log("searchBooklists constructor");
    }


    /**
         * Add the header to the page and load the BookTrackerClient.
         */
        mount() {
            // Wire up the form's 'submit' event and the button's 'click' event to the search method.
            document.getElementById('search-booklists-form').addEventListener('submit', this.search);
            document.getElementById('search-btn').addEventListener('click', this.search);
            document.getElementById('search-results-display').addEventListener("click", this.remove);

            this.header.addHeaderToPage();

            this.client = new BookTrackerClient();
        }


        /**
             * Uses the client to perform the search,
             * then updates the datastore with the criteria and results.
             * @param evt The "event" object representing the user-initiated event that triggered this method.
             */
            async search(evt) {
                // Prevent submitting the from from reloading the page.
                evt.preventDefault();

                const searchCriteria = document.getElementById('search-criteria').value;
                const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

                // If the user didn't change the search criteria, do nothing
                if (previousSearchCriteria === searchCriteria) {
                    return;
                }

                if (searchCriteria) {
                    const results = await this.client.search(searchCriteria);

                    this.dataStore.setState({
                        [SEARCH_CRITERIA_KEY]: searchCriteria,
                        [SEARCH_RESULTS_KEY]: results,
                    });
                } else {
                    this.dataStore.setState(EMPTY_DATASTORE_STATE);
                }
            }

    /**
         * Pulls search results from the datastore and displays them on the html page.
         */
        displaySearchResults() {
            const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
            const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

            const searchResultsContainer = document.getElementById('search-results-container');
            const searchCriteriaDisplay = document.getElementById('search-criteria-display');
            const searchResultsDisplay = document.getElementById('search-results-display');

            if (searchCriteria === '') {
                searchResultsContainer.classList.add('hidden');
                searchCriteriaDisplay.innerHTML = '';
                searchResultsDisplay.innerHTML = '';
            } else {
                searchResultsContainer.classList.remove('hidden');
                searchCriteriaDisplay.innerHTML = `"${searchCriteria}"`;
                searchResultsDisplay.innerHTML = this.getHTMLForSearchResults(searchResults);
            }
        }

    /**
         * Create appropriate HTML for displaying searchResults on the page.
         * @param searchResults An array of booklists objects to be displayed on the page.
         * @returns A string of HTML suitable for being dropped on the page.
         */
        getHTMLForSearchResults(searchResults) {
            if (searchResults.length === 0) {
                return '<h4>No results found</h4>';
            }

            let html = '<table><tr><th>Name</th><th>Book Count</th><th>Tags</th><th>Remove Booklist</th></tr>';
            for (const res of searchResults) {
                html += `
                <tr id= "${res.id}">
                    <td>
                        <a href="booklist.html?id=${res.id}">${res.name}</a>
                    </td>
                    <td>${res.bookCount}</td>
                    <td>${res.tags?.join(', ')}</td>
                    <td><button data-id="${res.id}" class="button remove-booklist">Remove ${res.name}</button></td>
                </tr>`;
            }
            html += '</table>';

            return html;
        }

       /**
         * when remove button is clicked, removes booklist.
         */
         async remove(e) {
               const removeButton = e.target;
               if (!removeButton.classList.contains('remove-booklist')) {
                    return;
               }

               removeButton.innerText = "Removing...";

             const errorMessageDisplay = document.getElementById('error-message');
             errorMessageDisplay.innerText = ``;
             errorMessageDisplay.classList.add('hidden');

             await this.client.removeBooklist(removeButton.dataset.id, (error) => {
               errorMessageDisplay.innerText = `Error: ${error.message}`;
               errorMessageDisplay.classList.remove('hidden');
             });

                 document.getElementById(removeButton.dataset.id).remove()
              }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const searchBooklists = new SearchBooklists();
    searchBooklists.mount();
};

window.addEventListener('DOMContentLoaded', main);