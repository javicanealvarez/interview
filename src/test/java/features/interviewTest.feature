
Feature: Block Chain Feature
  Scenario Outline: As a test user, I want the application to correctly chain the first 5 blocks starting from block number 15
    Given the test endpoint is available
    Then the request of the first "<numberOfBlocks>" blocks starting from block number "<startingBlockNumber>" to the endpoint is performed
    Then the response of the endpoint shows the first 5 blocks starting from block 15 correctly chained

    Examples:
    |startingBlockNumber|numberOfBlocks|
    | 15                |5             |

