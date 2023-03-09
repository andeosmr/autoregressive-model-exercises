--- A demonstration of the autoregressive model with some exercises ---

-- The demonstration --

You can checkout the 'solution' branch and run the main, and it will generate some schematic graphs that show the current form of the noise (noise.jpg), the autocorrelations for various lags (corrvector.jpg), the coefficients for the Yule-Walker method (ar.jpg), and the resulting form of the noise if these coefficients are used to create a new noise (next_noise.jpg).

You can change the value of the noise color (it is set to BROWN by default) in order to see the results for different noise colors.

[Currently the noise "color" sine is giving exponentially growing solutions, which is wrong]

-- The exercises --

- master branch -
This branch will only be able to show the noise.jpg correctly.
Fill in the gaps correctly in the code where it says #1, and you will be able to recreate the output from the 'autocorrelation' branch.

- autocorrelation branch -
This branch will correctly show noise.jpg and corrvector.jpg.
Fill in the gaps correctly in the code where it says #2, and you will be able to recreate the output from the 'autoregressive-coefficients' branch

- autoregressive-coefficients branch -
This branch will correctly show noise.jpg, corrvector.jpg and ar.jpg.
Fill in the gaps correctly in the code where it says #3, and you will be able to recreate the solution.