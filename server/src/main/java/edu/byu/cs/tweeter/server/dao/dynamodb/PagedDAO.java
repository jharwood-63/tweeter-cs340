//package edu.byu.cs.tweeter.server.dao.dynamodb;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
//import edu.byu.cs.tweeter.model.net.response.Response;
//import edu.byu.cs.tweeter.server.dao.dynamodb.bean.FollowBean;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.model.Page;
//import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
//import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
//import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
//
//public class PagedDAO<U> {
//    protected interface PagedRequestStrategy<U> {
//        void putSortKey(Map<String, AttributeValue> startKey, String sortKeyValue);
//        U createResponseObject();
//
//        void getDbData(U response, QueryEnhancedRequest request);
//
//        boolean validateResponse(U response);
//    }
//    private static boolean isNonEmptyString(String value) {
//        return (value != null && value.length() > 0);
//    }
//
//    protected U doPagedDbRequest(String targetUserAlias, int pageSize, String sortKeyValue, PagedRequestStrategy<U> pagedRequestStrategy,
//                                       String partitionKey) {
//        Key key = Key.builder()
//                .partitionValue(targetUserAlias)
//                .build();
//
//        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .limit(pageSize);
//
//        if (isNonEmptyString(sortKeyValue)) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(partitionKey, AttributeValue.builder().s(targetUserAlias).build());
//            pagedRequestStrategy.putSortKey(startKey, sortKeyValue);
//
//            requestBuilder.exclusiveStartKey(startKey);
//        }
//
//        QueryEnhancedRequest request = requestBuilder.build();
//
//        U response = pagedRequestStrategy.createResponseObject();
//
//        pagedRequestStrategy.getDbData(response, request);
//
//        if (!pagedRequestStrategy.validateResponse(response)) {
//            return new Response()
//        }
//
//        return response;
//    }
//
//    protected QueryEnhancedRequest createPagedQueryRequest(String targetUserAlias, int pageSize, String lastUserAlias, String partitionKey, String sortKey) {
//        Key key = Key.builder()
//                .partitionValue(targetUserAlias)
//                .build();
//
//        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
//                .queryConditional(QueryConditional.keyEqualTo(key))
//                .limit(pageSize);
//
//        if (isNonEmptyString(lastUserAlias)) {
//            Map<String, AttributeValue> startKey = new HashMap<>();
//            startKey.put(partitionKey, AttributeValue.builder().s(targetUserAlias).build());
//            startKey.put(sortKey, AttributeValue.builder().s(lastUserAlias).build());
//
//            requestBuilder.exclusiveStartKey(startKey);
//        }
//
//        return requestBuilder.build();
//    }
//}
