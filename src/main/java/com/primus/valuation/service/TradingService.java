package com.primus.valuation.service;

import com.primus.valuation.data.StockValuationData;
import java.util.*;
import com.primus.common.BusinessContext;

public interface TradingService {

  public List<StockValuationData> giveRecommendations(BusinessContext businessContext);

}
