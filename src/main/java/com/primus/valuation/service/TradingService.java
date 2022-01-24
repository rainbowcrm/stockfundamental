package com.primus.valuation.service;

import com.primus.valuation.data.StockValuationData;
import java.util.*;

public interface TradingService {

  public List<StockValuationData> giveRecommendations();

}
