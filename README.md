# Cryptocurrency Trading Bot

![Java](https://img.shields.io/badge/Java-11%2B-blue)
![License](https://img.shields.io/badge/license-MIT-green)

###### Made in 2023

This Java project implements a cryptocurrency trading bot that fetches real-time prices from Binance and utilizes multiple Relative Strength Index (RSI) strategies to make trading decisions. The bot is designed to perform buy and sell actions based on predefined RSI thresholds and strategies. It also offers extensibility to include additional strategies like Moving Average Convergence Divergence (MACD) and the ability to run multiple strategies concurrently.

## Trading Strategies

The trading bot currently implements the following RSI-based strategies:

- **Simple RSI Strategy**: Buys when RSI falls below a certain threshold and sells when RSI crosses another threshold.

- **RSI Divergence Strategy**: Looks for divergence between price and RSI values to predict potential trend reversals.

- **Custom Strategy**: Provides a framework to implement your custom trading strategy using RSI signals.

## Features

- Fetching real-time cryptocurrency prices from the Binance API.
- Multiple RSI-based trading strategies with customizable thresholds.
- Ability to easily add new trading strategies, like MACD or custom indicators.
- Concurrent execution of multiple strategies for diversified trading.
