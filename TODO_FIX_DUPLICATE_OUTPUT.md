# TODO: Fix Duplicate Output Issue

## Problem
When user says "Really?", the response had duplicate follow-up questions:
- Base response: "I see! Is there something specific I can help you with?"
- Plus contextual suffix: "Is there anything else you'd like to talk about?"
- Result: Two questions at the end

## Root Cause
The `ContextAwareResponseLogic.getContextualSuffixOnly()` method was adding follow-up questions even when the base response already contained a question.

## Solution
Modified `getContextualSuffixOnly()` to check if the base response already contains a question mark ("?") before adding contextual suffixes.

## Changes Made

### src/ContextAwareResponseLogic.java
1. Updated method signature to accept `baseResponse` parameter:
   ```java
   private String getContextualSuffixOnly(String baseResponse, EmotionDetector.Emotion emotion, ConversationContext context)
   ```

2. Added check at the beginning:
   ```java
   // Don't add follow-up if base response already contains a question
   if (baseResponse != null && baseResponse.contains("?")) {
       return "";
   }
   ```

3. Updated the call site in `generateContextualResponse()`:
   ```java
   String contextualSuffix = getContextualSuffixOnly(baseResponse, emotion, context);
   ```

## Status: COMPLETED ✓

**Verification:**
- All 71 unit tests pass ✓
- Code compiles successfully ✓
- Duplicate follow-up questions are now prevented
</parameter>
</invoke>
</minimax:tool_call>
